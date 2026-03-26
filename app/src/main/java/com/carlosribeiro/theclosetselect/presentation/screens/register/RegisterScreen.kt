package com.carlosribeiro.theclosetselect.presentation.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue // ADICIONE ESTE IMPORT MANUALMENTE
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlosribeiro.theclosetselect.presentation.components.AuraButton
import com.carlosribeiro.theclosetselect.presentation.components.AuraTextField
import com.carlosribeiro.theclosetselect.presentation.theme.DeepBlack
import com.carlosribeiro.theclosetselect.presentation.theme.GoldAura
import com.carlosribeiro.theclosetselect.presentation.theme.TheClosetSelectTheme

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState() // O import 'getValue' resolve o erro aqui

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("CRIAR CONTA", color = GoldAura, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 32.dp))

        AuraTextField(uiState.name, viewModel::onNameChange, "NOME COMPLETO")
        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AuraTextField(uiState.birthDate, viewModel::onDateOfBirthChange, "NASCIMENTO", Modifier.weight(1.5f))
            AuraTextField(uiState.zodiacSign, {}, "SIGNO", Modifier.weight(1f), enabled = false)
        }

        Spacer(Modifier.height(16.dp))
        AuraTextField(uiState.password, viewModel::onPasswordChange, "SENHA", visualTransformation = PasswordVisualTransformation())
        Spacer(Modifier.height(40.dp))
        AuraButton("CADASTRAR", viewModel::onRegisterClick)
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    TheClosetSelectTheme {
        RegisterScreen(viewModel = RegisterViewModel(), onBackClick = {})
    }
}