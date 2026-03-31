package com.carlosribeiro.theclosetselect.presentation.screens.forgotpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carlosribeiro.theclosetselect.presentation.components.AuraButton
import com.carlosribeiro.theclosetselect.presentation.components.AuraTextField
import com.carlosribeiro.theclosetselect.presentation.screens.login.LoginViewModel

private val BackgroundColor = Color(0xFF0D0D0D)
private val GoldColor = Color(0xFFB8972A)
private val SubtitleColor = Color(0xFF888888)
private val PinkColor = Color(0xFFE91E8C)
private val ErrorColor = Color(0xFFCF6679)

@Composable
fun ForgotPasswordScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        IconButton(
            onClick = onNavigateToLogin,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = PinkColor
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ForgotPasswordHeader()

            Spacer(modifier = Modifier.height(48.dp))

            AuraTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = "E-MAIL",
                placeholder = "seu@email.com"
            )

            if (uiState.errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.errorMessage!!,
                    color = ErrorColor,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = GoldColor,
                    modifier = Modifier.size(40.dp)
                )
            } else {
                AuraButton(
                    text = "ENVIAR LINK",
                    onClick = {
                        viewModel.onForgotPasswordClick(uiState.email)
                        onNavigateToLogin()
                    }
                )
            }
        }
    }
}

@Composable
private fun ForgotPasswordHeader() {
    Text(
        text = "✦",
        color = GoldColor,
        fontSize = 28.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "RECUPERAR ACESSO",
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 3.sp,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = "Informe seu e-mail cadastrado e enviaremos um link para redefinir sua senha.",
        color = SubtitleColor,
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        lineHeight = 20.sp
    )
}