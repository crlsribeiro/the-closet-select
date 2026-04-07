package com.carlosribeiro.theclosetselect.presentation.screens.login

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
import androidx.compose.ui.platform.LocalContext
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
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carlosribeiro.theclosetselect.BuildConfig
import com.carlosribeiro.theclosetselect.R
import com.carlosribeiro.theclosetselect.presentation.components.AuraTextField
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

// ── Dark / Black Theme ────────────────────────────────────────────────────────
private val Gold         = Color(0xFFC9A84C)
private val GoldLight    = Color(0xFFE2C97A)
private val ScreenBg     = Color(0xFF0D0D0D)
private val GoogleBg     = Color(0xFF1A1A1A)
private val DividerColor = Color(0xFF2E2E2E)
private val TextPrimary  = Color(0xFFF0ECE4)
private val TextMuted    = Color(0xFF888880)
private val ErrorColor   = Color(0xFFCF6679)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState         by viewModel.uiState.collectAsState()
    val context         = LocalContext.current
    val scope           = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onNavigateToHome()
            viewModel.clearError()
        }
    }

    LoginContent(
        uiState          = uiState,
        onEmailChange    = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick     = viewModel::onLoginClick,
        onForgotPassword = onNavigateToForgotPassword,
        onNavigateToRegister = onNavigateToRegister,
        onGoogleClick    = {
            viewModel.onGoogleSignInStarted()
            scope.launch {
                try {
                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                        .build()

                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                    val result = credentialManager.getCredential(
                        request = request,
                        context = context
                    )

                    val googleCredential = GoogleIdTokenCredential
                        .createFrom(result.credential.data)

                    viewModel.onGoogleSignInResult(googleCredential.idToken)

                } catch (e: GetCredentialCancellationException) {
                    // Usuário cancelou — limpa loading sem mostrar erro
                    viewModel.clearError()
                } catch (e: GetCredentialException) {
                    viewModel.onGoogleSignInError()
                }
            }
        }
    )
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onForgotPassword: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
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
            verticalArrangement   = Arrangement.Center,
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {

            // ── Cabeçalho ────────────────────────────────────────────────────
            Icon(
                painter            = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo",
                tint               = Gold,
                modifier           = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text          = "THE CLOSET SELECT",
                color         = Gold,
                fontSize      = 11.sp,
                fontWeight    = FontWeight.Medium,
                letterSpacing = 4.sp,
                textAlign     = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text       = "Welcome Back",
                color      = TextPrimary,
                fontSize   = 34.sp,
                fontWeight = FontWeight.Bold,
                fontStyle  = FontStyle.Italic,
                textAlign  = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text       = "Enter your credentials to access your atelier.",
                color      = TextMuted,
                fontSize   = 13.sp,
                textAlign  = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // ── Campos ───────────────────────────────────────────────────────
            AuraTextField(
                value         = uiState.email,
                onValueChange = onEmailChange,
                label         = "E-mail address"
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuraTextField(
                value         = uiState.password,
                onValueChange = onPasswordChange,
                label         = "Password",
                isPassword    = true
            )

            // Erro — val local para evitar smart cast em propriedade de StateFlow
            val error = uiState.errorMessage
            if (error != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text      = error,
                    color     = ErrorColor,
                    fontSize  = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Botões ───────────────────────────────────────────────────────
            if (uiState.isLoading) {
                CircularProgressIndicator(color = Gold, modifier = Modifier.size(40.dp))
            } else {

                // Sign In
                Button(
                    onClick        = onLoginClick,
                    modifier       = Modifier.fillMaxWidth().height(56.dp),
                    shape          = RoundedCornerShape(6.dp),
                    colors         = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.horizontalGradient(listOf(Gold, GoldLight, Gold))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text          = "SIGN IN",
                            color         = Color(0xFF1A1200),
                            fontSize      = 14.sp,
                            fontWeight    = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick  = onForgotPassword,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Forgot password?", color = Gold, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Divisor "or"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier          = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = DividerColor, thickness = 0.5.dp)
                    Text("  or  ", color = TextMuted, fontSize = 12.sp)
                    HorizontalDivider(modifier = Modifier.weight(1f), color = DividerColor, thickness = 0.5.dp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Continue with Google
                OutlinedButton(
                    onClick  = onGoogleClick,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape    = RoundedCornerShape(10.dp),
                    border   = androidx.compose.foundation.BorderStroke(0.5.dp, DividerColor),
                    colors   = ButtonDefaults.outlinedButtonColors(containerColor = GoogleBg)
                ) {
                    Row(
                        verticalAlignment    = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier             = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter            = painterResource(id = R.drawable.ic_google),
                            contentDescription = "Google",
                            tint               = Color.Unspecified,
                            modifier           = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text       = "Continue with Google",
                            color      = TextPrimary,
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            TextButton(
                onClick  = onNavigateToRegister,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = TextMuted, fontSize = 13.sp)) {
                            append("Don't have an account? ")
                        }
                        withStyle(SpanStyle(color = Gold, fontSize = 13.sp, fontWeight = FontWeight.Bold)) {
                            append("Sign up")
                        }
                    }
                )
            }
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF0D0D0D)
@Composable
private fun LoginScreenPreview() {
    LoginContent(
        uiState              = LoginUiState(),
        onEmailChange        = {},
        onPasswordChange     = {},
        onLoginClick         = {},
        onGoogleClick        = {},
        onForgotPassword     = {},
        onNavigateToRegister = {}
    )
}

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF0D0D0D, name = "Login Loading")
@Composable
private fun LoginScreenLoadingPreview() {
    LoginContent(
        uiState              = LoginUiState(isLoading = true),
        onEmailChange        = {},
        onPasswordChange     = {},
        onLoginClick         = {},
        onGoogleClick        = {},
        onForgotPassword     = {},
        onNavigateToRegister = {}
    )
}