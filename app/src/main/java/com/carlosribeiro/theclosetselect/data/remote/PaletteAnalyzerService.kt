package com.carlosribeiro.theclosetselect.data.remote

import android.util.Log
import com.carlosribeiro.theclosetselect.BuildConfig
import com.carlosribeiro.theclosetselect.domain.model.PaletteColor
import com.carlosribeiro.theclosetselect.domain.model.PaletteResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

private const val TAG = "PaletteAnalyzer"
private const val GEMINI_MODEL = "gemini-2.5-flash"

data class QuizAnswers(
    val eyeColor: String = "",
    val hairColor: String = "",
    val skinTone: String = "",
    val veinColor: String = "",
    val flatteringColors: String = "",
    val whiteEffect: String = "",
    val jewelryPreference: String = "",
    val sunReaction: String = ""
)

class PaletteAnalyzerService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun analyzePalette(quiz: QuizAnswers): Result<PaletteResult> {
        return withContext(Dispatchers.IO) {
            try {
                val apiKey = BuildConfig.GEMINI_API_KEY
                Log.d(TAG, "Gemini API Key length: ${apiKey.length}")

                if (apiKey.isBlank()) {
                    Log.e(TAG, "GEMINI_API_KEY is empty!")
                    return@withContext Result.failure(Exception("API key não configurada."))
                }

                val requestBody = buildRequestBody(quiz)
                Log.d(TAG, "Sending request to Gemini...")

                val response = executeRequest(requestBody, apiKey)
                Log.d(TAG, "Response received, parsing...")

                val result = parseResponse(response)
                Log.d(TAG, "Parse successful: ${result.seasonType}")

                Result.success(result)
            } catch (exception: Exception) {
                Log.e(TAG, "Error analyzing palette", exception)
                Result.failure(exception)
            }
        }
    }

    private fun buildRequestBody(quiz: QuizAnswers): String {
        val prompt = buildPrompt(quiz)

        val textPart = JSONObject().apply {
            put("text", prompt)
        }
        val parts = JSONArray().apply { put(textPart) }
        val content = JSONObject().apply { put("parts", parts) }
        val contents = JSONArray().apply { put(content) }

        return JSONObject().apply {
            put("contents", contents)
            put("generationConfig", JSONObject().apply {
                put("temperature", 0.3)
                put("maxOutputTokens", 8192)
            })
        }.toString()
    }

    private fun buildPrompt(quiz: QuizAnswers): String {
        return """
            Você é um especialista em coloração pessoal sazonal.
            Com base nas respostas abaixo, determine a estação de coloração pessoal e retorne APENAS JSON puro sem markdown.

            Respostas do questionário:
            - Cor dos olhos: ${quiz.eyeColor}
            - Cor do cabelo: ${quiz.hairColor}
            - Tom de pele: ${quiz.skinTone}
            - Veias do pulso: ${quiz.veinColor}
            - Cores que realçam: ${quiz.flatteringColors}
            - Efeito do branco puro no rosto: ${quiz.whiteEffect}
            - Preferência de joias: ${quiz.jewelryPreference}
            - Reação ao sol: ${quiz.sunReaction}

            Retorne APENAS este JSON:
            {
              "seasonType": "Winter ou Summer ou Spring ou Autumn",
              "subtom": "Frio ou Quente ou Neutro",
              "descricao": "Uma frase descrevendo a coloração pessoal desta pessoa",
              "bestColors": [
                {"nome": "Nome em português", "hex": "#RRGGBB"},
                {"nome": "Nome em português", "hex": "#RRGGBB"},
                {"nome": "Nome em português", "hex": "#RRGGBB"},
                {"nome": "Nome em português", "hex": "#RRGGBB"},
                {"nome": "Nome em português", "hex": "#RRGGBB"},
                {"nome": "Nome em português", "hex": "#RRGGBB"}
              ],
              "colorsToAvoid": [
                {"nome": "Nome em português", "hex": "#RRGGBB"},
                {"nome": "Nome em português", "hex": "#RRGGBB"},
                {"nome": "Nome em português", "hex": "#RRGGBB"}
              ]
            }
        """.trimIndent()
    }

    private fun executeRequest(requestBody: String, apiKey: String): String {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/$GEMINI_MODEL:generateContent?key=$apiKey"
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .post(requestBody.toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: throw Exception("Empty response from Gemini")

        Log.d(TAG, "HTTP Status: ${response.code}")

        if (!response.isSuccessful) {
            Log.e(TAG, "API Error body: $body")
            throw Exception("Gemini API error ${response.code}: $body")
        }

        return body
    }

    private fun parseResponse(responseBody: String): PaletteResult {
        val root = JSONObject(responseBody)
        val text = root
            .getJSONArray("candidates")
            .getJSONObject(0)
            .getJSONObject("content")
            .getJSONArray("parts")
            .getJSONObject(0)
            .getString("text")
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()

        Log.d(TAG, "Gemini response: $text")

        val paletteJson = JSONObject(text)

        val seasonType = paletteJson.optString("seasonType", "Autumn")
        val subtom = paletteJson.optString("subtom", "Neutro")
        val descricao = paletteJson.optString("descricao", "")

        val bestColors = parseColors(paletteJson.optJSONArray("bestColors"))
        val colorsToAvoid = parseColors(paletteJson.optJSONArray("colorsToAvoid"))

        return PaletteResult(
            seasonType = seasonType,
            subtom = subtom,
            descricao = descricao,
            bestColors = bestColors,
            colorsToAvoid = colorsToAvoid
        )
    }

    private fun parseColors(array: JSONArray?): List<PaletteColor> {
        if (array == null) return emptyList()
        return (0 until array.length()).map { i ->
            val obj = array.getJSONObject(i)
            PaletteColor(
                nome = obj.optString("nome", ""),
                hex = obj.optString("hex", "#000000")
            )
        }
    }
}