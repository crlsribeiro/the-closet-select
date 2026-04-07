package com.carlosribeiro.theclosetselect.presentation.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carlosribeiro.theclosetselect.R
import com.carlosribeiro.theclosetselect.presentation.components.AuraTextField
import kotlinx.coroutines.delay

// ── Dark / Black Theme ────────────────────────────────────────────────────────
private val Gold         = Color(0xFFC9A84C)
private val GoldLight    = Color(0xFFE2C97A)
private val ScreenBg     = Color(0xFF0D0D0D)
private val TextPrimary  = Color(0xFFF0ECE4)
private val TextMuted    = Color(0xFF888880)
private val ErrorColor   = Color(0xFFCF6679)
private val SuccessColor = Color(0xFF4CAF50)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun RegisterScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            showSuccess = true
            delay(2000)
            onNavigateToHome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Cabeçalho ────────────────────────────────────────────────────
            Icon(
                painter            = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo",
                tint               = Gold,
                modifier           = Modifier.size(44.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text          = "THE CLOSET SELECT",
                color         = Gold,
                fontSize      = 13.sp,
                fontWeight    = FontWeight.Medium,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text       = "Create Account",
                color      = TextPrimary,
                fontSize   = 38.sp,
                fontWeight = FontWeight.Bold,
                fontStyle  = FontStyle.Italic,
                textAlign  = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text      = "Join your personal atelier.",
                color     = TextMuted,
                fontSize  = 15.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            // ── Campos ───────────────────────────────────────────────────────
            RegisterField(
                label         = "FIRST NAME",
                value         = uiState.firstName,
                onValueChange = viewModel::onFirstNameChange,
                placeholder   = "Your first name"
            )
            Spacer(modifier = Modifier.height(20.dp))

            RegisterField(
                label         = "LAST NAME",
                value         = uiState.lastName,
                onValueChange = viewModel::onLastNameChange,
                placeholder   = "Your last name"
            )
            Spacer(modifier = Modifier.height(20.dp))

            FieldBlock(label = "E-MAIL ADDRESS") {
                AuraTextField(
                    value         = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    label         = "executive@atelier.com"
                )
                if (uiState.email.isNotEmpty() && !uiState.email.contains("@")) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "E-mail inválido", color = ErrorColor, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Data de nascimento — usa overload TextFieldValue para cursor correto
            FieldBlock(label = "DATE OF BIRTH") {
                AuraTextField(
                    value         = uiState.birthDate,
                    onValueChange = viewModel::onDateOfBirthChange,
                    label         = uiState.dateHint
                )
                if (uiState.zodiacSign.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text          = "✦ ${uiState.zodiacSign}",
                        color         = Gold,
                        fontSize      = 11.sp,
                        fontWeight    = FontWeight.Medium,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            RegisterField(
                label         = "PASSWORD",
                value         = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                placeholder   = "••••••••",
                isPassword    = true
            )
            Spacer(modifier = Modifier.height(20.dp))

            FieldBlock(label = "CONFIRM PASSWORD") {
                AuraTextField(
                    value         = uiState.confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    label         = "••••••••",
                    isPassword    = true
                )
                if (uiState.confirmPassword.isNotEmpty() && uiState.password != uiState.confirmPassword) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "As senhas não coincidem", color = ErrorColor, fontSize = 11.sp)
                }
            }

            // Erro geral — val local para evitar smart cast em propriedade de StateFlow
            val error = uiState.errorMessage
            if (error != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text      = error,
                    color     = ErrorColor,
                    fontSize  = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier.fillMaxWidth()
                )
            }

            if (showSuccess) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text       = "✦ Usuário cadastrado com sucesso!",
                    color      = SuccessColor,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign  = TextAlign.Center,
                    modifier   = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Botão ────────────────────────────────────────────────────────
            if (uiState.isLoading) {
                CircularProgressIndicator(color = Gold, modifier = Modifier.size(44.dp))
            } else {
                Button(
                    onClick        = viewModel::onRegisterClick,
                    enabled        = uiState.canSave,
                    modifier       = Modifier.fillMaxWidth().height(56.dp),
                    shape          = RoundedCornerShape(6.dp),
                    colors         = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                if (uiState.canSave)
                                    Brush.horizontalGradient(listOf(Gold, GoldLight, Gold))
                                else
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF2A2A2A), Color(0xFF3A3A3A), Color(0xFF2A2A2A))
                                    )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text          = "CREATE ACCOUNT",
                            color         = if (uiState.canSave) Color(0xFF1A1200) else TextMuted,
                            fontSize      = 15.sp,
                            fontWeight    = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = TextMuted, fontSize = 14.sp)) {
                            append("Already have an account? ")
                        }
                        withStyle(SpanStyle(color = Gold, fontSize = 14.sp, fontWeight = FontWeight.Bold)) {
                            append("Sign in")
                        }
                    }
                )
            }
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────
@Composable
private fun FieldBlock(
    label: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text          = label,
            color         = TextMuted,
            fontSize      = 12.sp,
            fontWeight    = FontWeight.SemiBold,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun RegisterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    FieldBlock(label = label) {
        AuraTextField(
            value         = value,
            onValueChange = onValueChange,
            label         = placeholder,
            isPassword    = isPassword
        )
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF0D0D0D)
@Composable
private fun RegisterScreenPreview() {
    RegisterScreen(onNavigateToHome = {}, onNavigateToLogin = {})
}