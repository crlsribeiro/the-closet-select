package com.carlosribeiro.theclosetselect.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlosribeiro.theclosetselect.presentation.theme.GoldAura

// ── Dark / Black Theme ────────────────────────────────────────────────────────
private val InputBackground  = Color(0xFF1A1A1A)
private val InputTextColor   = Color(0xFFF0ECE4)
private val InputBorder      = Color(0xFF2E2E2E)
private val InputPlaceholder = Color(0xFF666660)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Versão padrão — usada em todos os campos de texto simples (email, senha, nome, etc).
 * Mantém a assinatura original para não quebrar nenhum call-site existente.
 */
@Composable
fun AuraTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isPassword: Boolean = false,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        label = null,
        placeholder = {
            Text(
                text = label.ifEmpty { placeholder },
                color = InputPlaceholder,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation()
        else VisualTransformation.None,
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = keyboardOptions,
        textStyle = TextStyle(
            color = InputTextColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor      = GoldAura,
            unfocusedBorderColor    = InputBorder,
            disabledBorderColor     = InputBorder.copy(alpha = 0.3f),
            focusedContainerColor   = InputBackground,
            unfocusedContainerColor = InputBackground,
            disabledContainerColor  = InputBackground,
            focusedTextColor        = InputTextColor,
            unfocusedTextColor      = InputTextColor,
            cursorColor             = GoldAura
        )
    )
}

/**
 * Versão com TextFieldValue — usada em campos com máscara (ex: data de nascimento).
 * Permite controle preciso da posição do cursor ao inserir caracteres automaticamente.
 */
@Composable
fun AuraTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        label = null,
        placeholder = {
            Text(
                text = label.ifEmpty { placeholder },
                color = InputPlaceholder,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        },
        visualTransformation = VisualTransformation.None,
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = keyboardOptions,
        textStyle = TextStyle(
            color = InputTextColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor      = GoldAura,
            unfocusedBorderColor    = InputBorder,
            disabledBorderColor     = InputBorder.copy(alpha = 0.3f),
            focusedContainerColor   = InputBackground,
            unfocusedContainerColor = InputBackground,
            disabledContainerColor  = InputBackground,
            focusedTextColor        = InputTextColor,
            unfocusedTextColor      = InputTextColor,
            cursorColor             = GoldAura
        )
    )
}

// ── Previews ──────────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF0D0D0D)
@Composable
private fun AuraTextFieldPreview() {
    AuraTextField(
        value = "",
        onValueChange = {},
        label = "E-mail address"
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0D0D)
@Composable
private fun AuraTextFieldDatePreview() {
    AuraTextField(
        value = TextFieldValue("18/10"),
        onValueChange = {},
        label = "Data de nascimento (DD/MM/AAAA)"
    )
}