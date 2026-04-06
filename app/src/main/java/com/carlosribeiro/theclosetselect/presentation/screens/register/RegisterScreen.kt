package com.carlosribeiro.theclosetselect.presentation.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carlosribeiro.theclosetselect.R
import com.carlosribeiro.theclosetselect.presentation.components.AuraTextField
import kotlinx.coroutines.delay

private val Gold        = Color(0xFFB8972A)
private val GoldLight   = Color(0xFFD4AF5A)
private val CardBg      = Color(0xCCE8E4DC)
private val TextPrimary = Color(0xFF000000)
private val TextMuted   = Color(0xFF444444)
private val ErrorColor  = Color(0xFFCF6679)
private val SuccessColor = Color(0xFF4CAF50)
private val CardShape   = RoundedCornerShape(24.dp)

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

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.bg_closet),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(modifier = Modifier.fillMaxSize().background(Color(0x44000000)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 40.dp)
                    .clip(CardShape)
                    .background(CardBg)
                    .padding(horizontal = 28.dp, vertical = 36.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_logo),
                        contentDescription = "Logo",
                        tint = Gold,
                        modifier = Modifier.size(44.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "THE CLOSET SELECT",
                        color = Gold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 4.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Create Account",
                        color = TextPrimary,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Join your personal atelier.",
                        color = TextMuted,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Campos
                    RegisterField("FIRST NAME", uiState.firstName, viewModel::onFirstNameChange, "Your first name", KeyboardType.Text)
                    Spacer(modifier = Modifier.height(20.dp))
                    RegisterField("LAST NAME", uiState.lastName, viewModel::onLastNameChange, "Your last name", KeyboardType.Text)
                    Spacer(modifier = Modifier.height(20.dp))

                    // Email com validação visual
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "E-MAIL ADDRESS", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        AuraTextField(
                            value = uiState.email,
                            onValueChange = viewModel::onEmailChange,
                            label = "executive@atelier.com",
                            isPassword = false
                        )
                        if (uiState.email.isNotEmpty() && !uiState.email.contains("@")) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "E-mail inválido",
                                color = ErrorColor,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Data de nascimento com formato por locale
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "DATE OF BIRTH",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AuraTextField(
                            value = uiState.birthDate,
                            onValueChange = viewModel::onDateOfBirthChange,
                            label = uiState.dateHint,
                            isPassword = false
                        )
                        if (uiState.zodiacSign.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "✦ ${uiState.zodiacSign}",
                                color = Gold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    RegisterField("PASSWORD", uiState.password, viewModel::onPasswordChange, "••••••••", KeyboardType.Password, isPassword = true)
                    Spacer(modifier = Modifier.height(20.dp))

                    // Confirm password com validação visual
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "CONFIRM PASSWORD", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        AuraTextField(
                            value = uiState.confirmPassword,
                            onValueChange = viewModel::onConfirmPasswordChange,
                            label = "••••••••",
                            isPassword = true
                        )
                        if (uiState.confirmPassword.isNotEmpty() && uiState.password != uiState.confirmPassword) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "As senhas não coincidem", color = ErrorColor, fontSize = 11.sp)
                        }
                    }

                    // Erro geral
                    if (uiState.errorMessage != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = uiState.errorMessage!!,
                            color = ErrorColor,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Sucesso
                    if (showSuccess) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "✦ Usuário cadastrado com sucesso!",
                            color = SuccessColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Gold, modifier = Modifier.size(44.dp))
                    } else {
                        Button(
                            onClick = viewModel::onRegisterClick,
                            enabled = uiState.canSave,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        if (uiState.canSave)
                                            Brush.horizontalGradient(listOf(Gold, GoldLight, Gold))
                                        else
                                            Brush.horizontalGradient(listOf(Color(0xFF888888), Color(0xFFAAAAAA), Color(0xFF888888)))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "CREATE ACCOUNT",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
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
                                withStyle(SpanStyle(color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)) {
                                    append("Sign in")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RegisterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color(0xFF000000), fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(8.dp))
        AuraTextField(
            value = value,
            onValueChange = onValueChange,
            label = placeholder,
            isPassword = isPassword
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    RegisterScreen(onNavigateToHome = {}, onNavigateToLogin = {})
}