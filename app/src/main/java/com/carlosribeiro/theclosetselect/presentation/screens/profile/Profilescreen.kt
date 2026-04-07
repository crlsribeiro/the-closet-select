package com.carlosribeiro.theclosetselect.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carlosribeiro.theclosetselect.presentation.components.AuraTextField
import com.google.firebase.auth.FirebaseAuth

// ── Cores ─────────────────────────────────────────────────────────────────────
private val BackgroundColor = Color(0xFF0D0D0D)
private val CardBackground  = Color(0xFF1A1A1A)
private val Gold            = Color(0xFFC9A84C)
private val GoldLight       = Color(0xFFE2C97A)
private val SubtitleColor   = Color(0xFF888888)
private val ErrorColor      = Color(0xFFCF6679)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun ProfileScreen(
    onNavigateToHome: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState     by viewModel.uiState.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser

    val isOnboarding = uiState.birthdate.text.isEmpty() && !uiState.isLoading

    LaunchedEffect(uiState.isSaveSuccess) {
        if (uiState.isSaveSuccess) {
            viewModel.clearSuccess()
            onNavigateToHome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {

            // ── Top bar ──────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                if (!isOnboarding) {
                    IconButton(
                        onClick = onNavigateToHome,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                }
                Text(
                    text = "THE CLOSET SELECT",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 3.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Avatar ───────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(listOf(Color(0xFF2A2418), Color(0xFF1A1A1A)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (currentUser?.displayName?.firstOrNull()
                        ?: currentUser?.email?.firstOrNull()
                        ?: '?').uppercaseChar().toString(),
                    color = Gold,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Título ───────────────────────────────────────────────────────
            if (isOnboarding) {
                Text(
                    text = "Bem-vindo",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Precisamos de mais um detalhe\npara personalizar sua experiência.",
                    color = SubtitleColor,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                )
            } else {
                Text(
                    text = "Meu Perfil",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = currentUser?.email ?: "",
                    color = SubtitleColor,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ── Card formulário ──────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(CardBackground)
                    .padding(20.dp)
            ) {
                Text(
                    text = "INFORMAÇÕES PESSOAIS",
                    color = Gold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Nome
                AuraTextField(
                    value = uiState.displayName,
                    onValueChange = viewModel::onDisplayNameChange,
                    label = "Nome de exibição"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Data de nascimento — usa overload TextFieldValue para cursor correto
                AuraTextField(
                    value = uiState.birthdate,
                    onValueChange = viewModel::onBirthdateChange,
                    label = "Data de nascimento (DD/MM/AAAA)"
                )

                // Preview do signo
                if (uiState.birthdate.text.length == 10) {
                    val sign = getZodiacSign(uiState.birthdate.text)
                    if (sign != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "✦  $sign",
                            color = Gold,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Erro — val local para evitar smart cast em propriedade de StateFlow
                val error = uiState.errorMessage
                if (error != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = error,
                        color = ErrorColor,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Botão salvar ─────────────────────────────────────────────────
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Gold, modifier = Modifier.size(40.dp))
                    }
                } else {
                    Button(
                        onClick = viewModel::onSaveClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(listOf(Gold, GoldLight, Gold))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "SALVAR",
                                color = Color(0xFF1A1200),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 3.sp
                            )
                        }
                    }
                }
            }

            // Dica extra só no onboarding
            if (isOnboarding) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Você pode atualizar isso a qualquer momento\nem Meu Perfil.",
                    color = SubtitleColor,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ── Utilitário: calcula signo a partir de "DD/MM/YYYY" ───────────────────────
private fun getZodiacSign(birthdate: String): String? {
    return try {
        val parts = birthdate.split("/")
        val day   = parts[0].toInt()
        val month = parts[1].toInt()
        when {
            (month == 3  && day >= 21) || (month == 4  && day <= 19) -> "Áries ♈"
            (month == 4  && day >= 20) || (month == 5  && day <= 20) -> "Touro ♉"
            (month == 5  && day >= 21) || (month == 6  && day <= 20) -> "Gêmeos ♊"
            (month == 6  && day >= 21) || (month == 7  && day <= 22) -> "Câncer ♋"
            (month == 7  && day >= 23) || (month == 8  && day <= 22) -> "Leão ♌"
            (month == 8  && day >= 23) || (month == 9  && day <= 22) -> "Virgem ♍"
            (month == 9  && day >= 23) || (month == 10 && day <= 22) -> "Libra ♎"
            (month == 10 && day >= 23) || (month == 11 && day <= 21) -> "Escorpião ♏"
            (month == 11 && day >= 22) || (month == 12 && day <= 21) -> "Sagitário ♐"
            (month == 12 && day >= 22) || (month == 1  && day <= 19) -> "Capricórnio ♑"
            (month == 1  && day >= 20) || (month == 2  && day <= 18) -> "Aquário ♒"
            (month == 2  && day >= 19) || (month == 3  && day <= 20) -> "Peixes ♓"
            else -> null
        }
    } catch (_: Exception) { null }
}