package com.carlosribeiro.theclosetselect.presentation.screens.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carlosribeiro.theclosetselect.R
import com.carlosribeiro.theclosetselect.presentation.components.AuraButton
import com.carlosribeiro.theclosetselect.presentation.components.AuraSecondaryButton
import com.carlosribeiro.theclosetselect.presentation.components.AuraTextField
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

private val BackgroundColor = Color(0xFF0D0D0D)
private val GoldColor = Color(0xFFB8972A)
private val SubtitleColor = Color(0xFF888888)
private val DividerColor = Color(0xFF333333)
private val ErrorColor = Color(0xFFCF6679)

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val googleSignInClient = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.google_web_client_id))
            .requestEmail()
            .build()
    )

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { viewModel.onGoogleSignInResult(it) }
            } catch (e: ApiException) {
                // erro silencioso — o ViewModel já trata
            }
        }
    }

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onNavigateToHome()
            viewModel.clearError()
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
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginHeader()

            Spacer(modifier = Modifier.height(48.dp))

            AuraTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = "E-MAIL"
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuraTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label = "PASSWORD",
                isPassword = true
            )

            ForgotPasswordButton(onNavigateToForgotPassword)

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

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = GoldColor,
                    modifier = Modifier.size(40.dp)
                )
            } else {
                AuraButton(
                    text = "SIGN IN",
                    onClick = viewModel::onLoginClick
                )

                Spacer(modifier = Modifier.height(24.dp))

                OrDivider()

                Spacer(modifier = Modifier.height(24.dp))

                AuraSecondaryButton(
                    text = "Continue with Google",
                    onClick = {
                        googleSignInClient.signOut()
                        googleLauncher.launch(googleSignInClient.signInIntent)
                    }
                )
            }

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
        HorizontalDivider(modifier = Modifier.weight(1f), color = DividerColor)
        Text(text = "  or  ", color = SubtitleColor, fontSize = 12.sp)
        HorizontalDivider(modifier = Modifier.weight(1f), color = DividerColor)
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