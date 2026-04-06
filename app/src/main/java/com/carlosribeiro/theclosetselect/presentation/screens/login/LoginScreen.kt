package com.carlosribeiro.theclosetselect.presentation.screens.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carlosribeiro.theclosetselect.R
import com.carlosribeiro.theclosetselect.presentation.components.AuraTextField
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

private val Gold        = Color(0xFFB8972A)
private val GoldLight   = Color(0xFFD4AF5A)
private val CardBg      = Color(0xCCE8E4DC)
private val TextPrimary = Color(0xFF1A1A1A)
private val TextMuted   = Color(0xFF6B6560)
private val FieldBorder = Color(0xFFB0A99E)
private val ErrorColor  = Color(0xFFCF6679)
private val CardShape   = RoundedCornerShape(24.dp)

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
            } catch (_: ApiException) {
                viewModel.clearError()
            }
        } else {
            viewModel.clearError()
        }
    }

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onNavigateToHome()
            viewModel.clearError()
        }
    }

    LoginContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::onLoginClick,
        onGoogleClick = {
            viewModel.onGoogleSignInStarted()
            googleSignInClient.signOut()
            googleLauncher.launch(googleSignInClient.signInIntent)
        },
        onForgotPassword = onNavigateToForgotPassword,
        onNavigateToRegister = onNavigateToRegister
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
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "THE CLOSET SELECT",
                        color = Gold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 4.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Welcome Back",
                        color = TextPrimary,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Enter your credentials to access your atelier.",
                        color = TextMuted,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    AuraTextField(
                        value = uiState.email,
                        onValueChange = onEmailChange,
                        label = "E-mail address",
                        placeholder = "executive@atelier.com"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AuraTextField(
                        value = uiState.password,
                        onValueChange = onPasswordChange,
                        label = "Password",
                        placeholder = "••••••••",
                        isPassword = true
                    )

                    if (uiState.errorMessage != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = uiState.errorMessage,
                            color = ErrorColor,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = Gold,
                            modifier = Modifier.size(40.dp)
                        )
                    } else {

                        Button(
                            onClick = onLoginClick,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
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
                                    text = "SIGN IN",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 3.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(onClick = onForgotPassword) {
                            Text("Forgot password?", color = Gold, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = FieldBorder)
                            Text("  or  ", color = TextMuted, fontSize = 12.sp)
                            HorizontalDivider(modifier = Modifier.weight(1f), color = FieldBorder)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = onGoogleClick,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, FieldBorder),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_google),
                                    contentDescription = "Google",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Continue with Google",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(onClick = onNavigateToRegister) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = TextMuted, fontSize = 13.sp)) {
                                    append("Don't have an account? ")
                                }
                                withStyle(SpanStyle(color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)) {
                                    append("Sign up")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LoginContent(
        uiState = LoginUiState(),
        onEmailChange = {},
        onPasswordChange = {},
        onLoginClick = {},
        onGoogleClick = {},
        onForgotPassword = {},
        onNavigateToRegister = {}
    )
}

@Preview(showBackground = true, showSystemUi = true, name = "Login Loading")
@Composable
private fun LoginScreenLoadingPreview() {
    LoginContent(
        uiState = LoginUiState(isLoading = true),
        onEmailChange = {},
        onPasswordChange = {},
        onLoginClick = {},
        onGoogleClick = {},
        onForgotPassword = {},
        onNavigateToRegister = {}
    )
}