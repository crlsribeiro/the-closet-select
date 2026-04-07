package com.carlosribeiro.theclosetselect.presentation.screens.gerarlook

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.carlosribeiro.theclosetselect.BuildConfig
import com.carlosribeiro.theclosetselect.data.model.WeatherResponse
import com.carlosribeiro.theclosetselect.data.remote.WeatherService
import com.carlosribeiro.theclosetselect.data.repository.GarmentRepositoryImpl
import com.carlosribeiro.theclosetselect.data.repository.PaletteRepositoryImpl
import com.carlosribeiro.theclosetselect.domain.model.Garment
import com.carlosribeiro.theclosetselect.domain.model.PaletteResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class Look(
    val title: String = "",
    val description: String = "",
    val pieces: List<String> = emptyList(),
    val colorTip: String = ""
)

data class GerarLookState(
    val weather: WeatherResponse? = null,
    val isLoadingWeather: Boolean = true,
    val isGpsDisabled: Boolean = false,
    val looks: List<Look> = emptyList(),
    val isGenerating: Boolean = false,
    val isGenerated: Boolean = false,
    val errorMessage: String? = null,
    val garmentCount: Int = 0,
    val hasEnoughGarments: Boolean = false
)

class GerarLookViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(GerarLookState())
    val uiState: StateFlow<GerarLookState> = _uiState.asStateFlow()

    private val userId              = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    private val weatherService      = WeatherService()
    private val paletteRepository   = PaletteRepositoryImpl()
    private val garmentRepository   = GarmentRepositoryImpl(application)
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    init {
        loadWeather()
        loadGarmentCount()
    }

    private fun isGpsEnabled(): Boolean {
        val locationManager = getApplication<Application>()
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun loadWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingWeather = true, isGpsDisabled = false) }

            if (!isGpsEnabled()) {
                _uiState.update { it.copy(isLoadingWeather = false, isGpsDisabled = true) }
                return@launch
            }

            try {
                val location = fusedLocationClient
                    .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                    .await()

                if (location != null) {
                    weatherService.getWeather(location.latitude, location.longitude)
                        .onSuccess { weather ->
                            _uiState.update {
                                it.copy(weather = weather, isLoadingWeather = false, isGpsDisabled = false)
                            }
                        }
                        .onFailure {
                            _uiState.update { it.copy(isLoadingWeather = false) }
                        }
                } else {
                    _uiState.update { it.copy(isLoadingWeather = false, isGpsDisabled = true) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingWeather = false) }
            }
        }
    }

    fun loadWeatherIfNeeded() {
        if (_uiState.value.weather == null) loadWeather()
    }

    fun openLocationSettings(context: Context) {
        context.startActivity(
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }

    private fun loadGarmentCount() {
        viewModelScope.launch {
            garmentRepository.getGarments(userId).collect { garments ->
                _uiState.update {
                    it.copy(
                        garmentCount      = garments.size,
                        hasEnoughGarments = garments.size >= 3
                    )
                }
            }
        }
    }

    fun generateLooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true, errorMessage = null) }

            try {
                val userDoc = FirebaseFirestore.getInstance()
                    .collection("users").document(userId).get().await()
                val zodiacSign = userDoc.getString("zodiacSign") ?: "Leão"

                val palette  = paletteRepository.getPalette(userId)
                val garments = garmentRepository.getGarments(userId).first()

                val weather = _uiState.value.weather
                val looks   = generateWithGemini(
                    zodiacSign = zodiacSign,
                    weather    = weather,
                    palette    = palette,
                    garments   = garments
                )

                _uiState.update {
                    it.copy(looks = looks, isGenerating = false, isGenerated = true)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isGenerating = false, errorMessage = "Erro ao gerar looks. Tente novamente.")
                }
            }
        }
    }

    private suspend fun generateWithGemini(
        zodiacSign: String,
        weather: WeatherResponse?,
        palette: PaletteResult?,
        garments: List<Garment>
    ): List<Look> = withContext(Dispatchers.IO) {

        val weatherInfo = weather?.let {
            "Clima atual: ${it.condition}, ${it.tempFormatted}, sensação ${it.feelsLikeFormatted}, umidade ${it.humidity}%"
        } ?: "Clima não disponível"

        val paletteInfo = palette?.let {
            "Estação de coloração: ${it.seasonType}, Subtom: ${it.subtom}. " +
                    "Melhores cores: ${it.bestColors.joinToString(", ") { c -> c.nome }}"
        } ?: "Paleta não disponível"

        // Lista numerada para forçar o modelo a referenciar peças exatas
        val garmentsList = garments.mapIndexed { index, g ->
            "${index + 1}. ${g.name} (${g.type.displayName}, ${g.category.displayName})"
        }.joinToString("\n")

        val totalGarments = garments.size

        val prompt = """
            Você é uma consultora de moda especialista em coloração pessoal e estilo.
            
            Dados da usuária:
            - Signo: $zodiacSign
            - $weatherInfo
            - $paletteInfo
            
            Peças disponíveis no guarda-roupa ($totalGarments peças no total):
            $garmentsList
            
            REGRAS OBRIGATÓRIAS:
            1. Use APENAS os nomes exatos das peças da lista acima — não invente peças.
            2. Cada peça só pode aparecer UMA única vez dentro do mesmo look.
            3. Cada look deve ter no mínimo 2 peças e no máximo ${minOf(totalGarments, 4)} peças.
            4. Looks diferentes podem compartilhar peças entre si, mas nunca dentro do mesmo look.
            5. Os looks devem ser adequados para o clima atual.
            6. Priorize combinações alinhadas com a paleta de cores da usuária.
            
            Sugira 3 looks completos seguindo as regras acima.
            
            Retorne APENAS este JSON, sem texto adicional:
            {
              "looks": [
                {
                  "title": "Nome criativo do look",
                  "description": "Breve descrição do estilo e ocasião (máximo 2 frases)",
                  "pieces": ["Nome exato da peça 1", "Nome exato da peça 2"],
                  "colorTip": "Dica rápida sobre harmonia de cores com a paleta da usuária"
                }
              ]
            }
        """.trimIndent()

        val textPart = org.json.JSONObject().apply { put("text", prompt) }
        val parts    = org.json.JSONArray().apply { put(textPart) }
        val content  = org.json.JSONObject().apply { put("parts", parts) }
        val contents = org.json.JSONArray().apply { put(content) }
        val body     = org.json.JSONObject().apply { put("contents", contents) }.toString()

        val apiKey = BuildConfig.GEMINI_API_KEY
        val url    = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()

        val response     = httpClient.newCall(request).execute()
        val responseBody = response.body?.string() ?: throw Exception("Empty response")
        if (!response.isSuccessful) throw Exception("Gemini error ${response.code}")

        val root = JSONObject(responseBody)
        val text = root.getJSONArray("candidates")
            .getJSONObject(0)
            .getJSONObject("content")
            .getJSONArray("parts")
            .getJSONObject(0)
            .getString("text")
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()

        val looksJson = JSONObject(text).getJSONArray("looks")
        (0 until looksJson.length()).map { i ->
            val look        = looksJson.getJSONObject(i)
            val piecesArray = look.getJSONArray("pieces")
            Look(
                title       = look.getString("title"),
                description = look.getString("description"),
                pieces      = (0 until piecesArray.length()).map { piecesArray.getString(it) },
                colorTip    = look.getString("colorTip")
            )
        }
    }

    fun onDismissError() = _uiState.update { it.copy(errorMessage = null) }
    fun onReset()        = _uiState.update { it.copy(looks = emptyList(), isGenerated = false) }
}