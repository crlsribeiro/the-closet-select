package com.carlosribeiro.theclosetselect.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlosribeiro.theclosetselect.presentation.components.AuraButton
import com.carlosribeiro.theclosetselect.presentation.components.AuraSecondaryButton
import com.carlosribeiro.theclosetselect.presentation.components.AuraTextField

private val BackgroundColor = Color(0xFF0D0D0D)
private val GoldColor = Color(0xFFB8972A)
private val SubtitleColor = Color(0xFF888888)
private val DividerColor = Color(0xFF333333)

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginHeader()

            Spacer(modifier = Modifier.height(48.dp))

            AuraTextField(
                value = email,
                onValueChange = { email = it },
                label = "E-MAIL"
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuraTextField(
                value = password,
                onValueChange = { password = it },
                label = "PASSWORD",
                isPassword = true
            )

            ForgotPasswordButton(onNavigateToForgotPassword)

            Spacer(modifier = Modifier.height(32.dp))

            AuraButton(
                text = "SIGN IN",
                onClick = onNavigateToHome
            )

            Spacer(modifier = Modifier.height(24.dp))

            OrDivider()

            Spacer(modifier = Modifier.height(24.dp))

            AuraSecondaryButton(
                text = "Continue with Google",
                onClick = { /* TODO: Google Sign-In */ }
            )

            Spacer(modifier = Modifier.height(32.dp))

            RegisterFooter(onNavigateToRegister)
        }
    }
}

@Composable
private fun LoginHeader() {
    Text(
        text = "THE CLOSET\nSELECT",
        color = GoldColor,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 6.sp,
        lineHeight = 40.sp,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "YOUR SMART CLOSET",
        color = SubtitleColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.Light,
        letterSpacing = 3.sp
    )
}

@Composable
private fun ForgotPasswordButton(onNavigateToForgotPassword: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            onClick = onNavigateToForgotPassword,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Text(
                text = "Forgot my password",
                color = SubtitleColor,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun OrDivider() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = DividerColor
        )
        Text(
            text = "  or  ",
            color = SubtitleColor,
            fontSize = 12.sp
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = DividerColor
        )
    }
}

@Composable
private fun RegisterFooter(onNavigateToRegister: () -> Unit) {
    TextButton(onClick = onNavigateToRegister) {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = SubtitleColor, fontSize = 13.sp)) {
                    append("Don't have an account? ")
                }
                withStyle(SpanStyle(color = GoldColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)) {
                    append("CREATE ACCOUNT")
                }
            }
        )
    }
}