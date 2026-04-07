package com.carlosribeiro.theclosetselect.presentation.screens.gerarlook

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carlosribeiro.theclosetselect.data.model.WeatherResponse
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

private val BackgroundColor = Color(0xFF0D0D0D)
private val CardBackground  = Color(0xFF1A1A1A)
private val Gold            = Color(0xFFB8972A)
private val TextMuted       = Color(0xFF888888)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GerarLookScreen(onNavigateBack: () -> Unit = {}) {
    val context = LocalContext.current
    val viewModel: GerarLookViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
            .getInstance(context.applicationContext as android.app.Application)
    )
    val uiState by viewModel.uiState.collectAsState()

    val locationPermission = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(locationPermission.status.isGranted) {
        if (locationPermission.status.isGranted) {
            viewModel.loadWeatherIfNeeded()
        } else {
            locationPermission.launchPermissionRequest()
        }
    }

    // Recarrega clima quando volta das configurações
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                if (locationPermission.status.isGranted) {
                    viewModel.loadWeather()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "GERAR LOOK",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 3.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
        }

        item {
            WeatherCard(
                weather = uiState.weather,
                isLoading = uiState.isLoadingWeather,
                isGpsDisabled = uiState.isGpsDisabled,
                onEnableGps = { viewModel.openLocationSettings(context) }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        if (!uiState.isGenerated) {
            item {
                GenerateSection(
                    garmentCount = uiState.garmentCount,
                    hasEnoughGarments = uiState.hasEnoughGarments,
                    isGenerating = uiState.isGenerating,
                    onGenerate = viewModel::generateLooks
                )
            }
        } else {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SEUS LOOKS DO DIA",
                        color = Gold,
                        fontSize = 11.sp,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Medium
                    )
                    TextButton(onClick = viewModel::onReset) {
                        Text("Gerar novos", color = TextMuted, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(uiState.looks) { look ->
                LookCard(look = look)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }

    uiState.errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = viewModel::onDismissError,
            title = { Text("Atenção", color = Color.White) },
            text = { Text(message, color = TextMuted) },
            confirmButton = {
                TextButton(onClick = viewModel::onDismissError) {
                    Text("OK", color = Gold)
                }
            },
            containerColor = CardBackground
        )
    }
}

@Composable
private fun WeatherCard(
    weather: WeatherResponse?,
    isLoading: Boolean,
    isGpsDisabled: Boolean,
    onEnableGps: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(CardBackground)
            .padding(20.dp)
    ) {
        Text(
            text = "CLIMA HOJE",
            color = Gold,
            fontSize = 10.sp,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(12.dp))

        when {
            isLoading -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(color = Gold, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Obtendo localização...", color = TextMuted, fontSize = 13.sp)
                }
            }
            isGpsDisabled -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("📍 Localização desativada", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Ative o GPS para ver o clima", color = TextMuted, fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    TextButton(
                        onClick = onEnableGps,
                        colors = ButtonDefaults.textButtonColors(contentColor = Gold)
                    ) {
                        Text("ATIVAR", fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                }
            }
            weather == null -> {
                Text("Clima não disponível", color = TextMuted, fontSize = 13.sp)
            }
            else -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${weather.weatherEmoji} ${weather.tempFormatted}",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = weather.condition, color = TextMuted, fontSize = 13.sp)
                        Text(text = weather.feelsLikeFormatted, color = TextMuted, fontSize = 11.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = weather.cityName, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Text(text = "Umidade ${weather.humidity}%", color = TextMuted, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun GenerateSection(
    garmentCount: Int,
    hasEnoughGarments: Boolean,
    isGenerating: Boolean,
    onGenerate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "✦", color = Gold, fontSize = 32.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "CURADOR DE LOOKS", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 3.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Looks personalizados baseados no clima,\nseu signo e sua paleta de cores.",
            color = TextMuted, fontSize = 13.sp, textAlign = TextAlign.Center, lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(32.dp))

        if (!hasEnoughGarments) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardBackground)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "👗", fontSize = 32.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Você tem $garmentCount ${if (garmentCount == 1) "peça" else "peças"}",
                    color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Adicione pelo menos 3 peças ao seu arquivo\npara gerar looks personalizados.",
                    color = TextMuted, fontSize = 12.sp, textAlign = TextAlign.Center, lineHeight = 18.sp
                )
            }
        } else {
            if (isGenerating) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Gold, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Criando seus looks...", color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Analisando clima, paleta e guarda-roupa", color = TextMuted, fontSize = 15.sp, fontStyle = FontStyle.Italic)
                }
            } else {
                Button(
                    onClick = onGenerate,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold)
                ) {
                    Text("✦  GERAR MEUS LOOKS", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "$garmentCount peças disponíveis", color = TextMuted, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun LookCard(look: Look) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(CardBackground)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(text = look.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text(text = "✦", color = Gold, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = look.description, color = TextMuted, fontSize = 12.sp, lineHeight = 18.sp, fontStyle = FontStyle.Italic)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "PEÇAS", color = Gold, fontSize = 10.sp, letterSpacing = 1.5.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        look.pieces.forEach { piece ->
            Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(4.dp).background(Gold, shape = RoundedCornerShape(2.dp)))
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = piece, color = Color.White, fontSize = 13.sp)
            }
        }
        if (look.colorTip.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2C2C2C))
                    .padding(12.dp)
            ) {
                Text(text = "🎨 ${look.colorTip}", color = TextMuted, fontSize = 11.sp, lineHeight = 16.sp)
            }
        }
    }
}