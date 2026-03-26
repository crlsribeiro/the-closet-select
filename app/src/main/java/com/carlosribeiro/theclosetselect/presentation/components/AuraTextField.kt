package com.carlosribeiro.theclosetselect.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AuraTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false, // Agora o parâmetro existe!
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true,
        // Se for senha, esconde os caracteres, senão mostra normal
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        // Ajusta o teclado para tipo senha se necessário
        keyboardOptions = if (isPassword) {
            KeyboardOptions(keyboardType = KeyboardType.Password)
        } else {
            KeyboardOptions(keyboardType = KeyboardType.Text)
        }
    )
}