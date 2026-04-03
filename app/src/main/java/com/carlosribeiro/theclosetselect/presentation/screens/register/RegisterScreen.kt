package com.carlosribeiro.theclosetselect.presentation.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carlosribeiro.theclosetselect.R
import com.carlosribeiro.theclosetselect.presentation.components.AuraTextField

private val Gold        = Color(0xFFB8972A)
private val GoldLight   = Color(0xFFD4AF5A)
private val CardBg      = Color(0xCCE8E4DC)
private val TextPrimary = Color(0xFF000000)   // preto
private val TextLabel   = Color(0xFF000000)   // preto para labels
private val TextMuted   = Color(0xFF444444)   // quase preto para subtítulos
private val ErrorColor  = Color(0xFFCF6679)
private val CardShape   = RoundedCornerShape(24.dp)

@Composable
fun RegisterScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onNavigateToHome()
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

                    RegisterField("FIRST NAME",       uiState.firstName,       viewModel::onFirstNameChange,       "Your first name")
                    Spacer(modifier = Modifier.height(20.dp))
                    RegisterField("LAST NAME",         uiState.lastName,        viewModel::onLastNameChange,        "Your last name")
                    Spacer(modifier = Modifier.height(20.dp))
                    RegisterField("E-MAIL ADDRESS",    uiState.email,           viewModel::onEmailChange,           "executive@atelier.com")
                    Spacer(modifier = Modifier.height(20.dp))
                    RegisterField("DATE OF BIRTH",     uiState.birthDate,       viewModel::onDateOfBirthChange,     "DD/MM/YYYY")
                    Spacer(modifier = Modifier.height(20.dp))
                    RegisterField("PASSWORD",          uiState.password,        viewModel::onPasswordChange,        "••••••••", isPassword = true)
                    Spacer(modifier = Modifier.height(20.dp))
                    RegisterField("CONFIRM PASSWORD",  uiState.confirmPassword, viewModel::onConfirmPasswordChange, "••••••••", isPassword = true)

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
                                    .background(Brush.horizontalGradient(listOf(Gold, GoldLight, Gold))),
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
    isPassword: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = TextLabel,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        AuraTextField(
            value = value,
            onValueChange = onValueChange,
            label = placeholder,
            isPassword = isPassword
        )
    }
}