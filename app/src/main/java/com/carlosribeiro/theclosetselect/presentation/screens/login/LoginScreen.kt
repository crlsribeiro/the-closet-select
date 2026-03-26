package com.carlosribeiro.theclosetselect.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlosribeiro.theclosetselect.R
// ESTES IMPORTS ABAIXO RESOLVEM O ERRO DA IMAGEM
import com.carlosribeiro.theclosetselect.presentation.components.AuraButton
import com.carlosribeiro.theclosetselect.presentation.components.AuraSecondaryButton
import com.carlosribeiro.theclosetselect.presentation.components.AuraTextField
import com.carlosribeiro.theclosetselect.presentation.theme.DeepBlack
import com.carlosribeiro.theclosetselect.presentation.theme.GoldAura

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título estilizado conforme o design
        Text(
            text = stringResource(id = R.string.login_title),
            color = GoldAura,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 36.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(id = R.string.login_subtitle),
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
        )

        // Email
        AuraTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            label = stringResource(id = R.string.label_email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Senha
        AuraTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            label = stringResource(id = R.string.label_password),
            visualTransformation = PasswordVisualTransformation()
        )

        // Esqueci minha senha
        TextButton(
            onClick = onNavigateToForgotPassword,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = stringResource(id = R.string.btn_forgot_password),
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botão Entrar
        AuraButton(
            text = stringResource(id = R.string.btn_enter),
            onClick = viewModel::onLoginClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.label_or),
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botão Google
        AuraSecondaryButton(
            text = stringResource(id = R.string.btn_google),
            onClick = { /* Lógica Google */ }
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Link para Criar Conta
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.label_no_account),
                color = Color.Gray
            )
            TextButton(
                onClick = onNavigateToRegister,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.btn_create_account),
                    color = GoldAura,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}