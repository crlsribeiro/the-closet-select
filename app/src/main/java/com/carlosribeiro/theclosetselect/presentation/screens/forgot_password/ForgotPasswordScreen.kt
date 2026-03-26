package com.carlosribeiro.theclosetselect.presentation.screens.forgot_password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Import correto para evitar o Warning
import androidx.compose.material3.*
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
import com.carlosribeiro.theclosetselect.presentation.components.AuraButton
import com.carlosribeiro.theclosetselect.presentation.components.AuraTextField
import com.carlosribeiro.theclosetselect.presentation.theme.DeepBlack
import com.carlosribeiro.theclosetselect.presentation.theme.GoldAura

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    onBackClick: () -> Unit
) {
    val email by viewModel.email.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar com Seta de Voltar (Corrigida com AutoMirrored)
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = GoldAura
                )
            }
            Text(
                text = "THE CLOSET SELECT",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = GoldAura,
                fontSize = 12.sp, // Um pouco menor para elegância
                fontWeight = FontWeight.Light,
                letterSpacing = 2.sp
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "BEM-VINDO AO\nTHE CLOSET SELECT",
            color = GoldAura,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )

        Text(
            text = "Esqueci minha senha",
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Informe seu e-mail e enviaremos um link para redefinir sua senha.",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp, bottom = 40.dp, start = 20.dp, end = 20.dp)
        )

        AuraTextField(
            value = email,
            onValueChange = viewModel::onEmailChange,
            label = "E-MAIL"
        )

        Spacer(modifier = Modifier.height(32.dp))

        AuraButton(
            text = "ENVIAR LINK",
            onClick = viewModel::onSendClick
        )

        TextButton(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = "VOLTAR PARA O LOGIN",
                color = Color.Gray,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "© 2026 THE CLOSET SELECT. ALL RIGHTS RESERVED.", // Atualizado para 2026
            color = Color.DarkGray,
            fontSize = 10.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}