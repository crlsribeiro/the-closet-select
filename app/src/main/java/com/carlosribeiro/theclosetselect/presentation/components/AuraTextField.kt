package com.carlosribeiro.theclosetselect.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlosribeiro.theclosetselect.presentation.theme.GoldAura

private val InputBackground = Color(0xFFFFFFFF)
private val InputTextColor  = Color(0xFF1A1A1A)
private val InputBorder     = Color(0xFFCCC8BF)
private val InputLabel      = Color(0xFF6B6560)

@Composable
fun AuraTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isPassword: Boolean = false,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        label = {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = InputLabel
                )
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                color = InputLabel.copy(alpha = 0.5f),
                fontSize = 14.sp
            )
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        textStyle = TextStyle(
            color = InputTextColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GoldAura,
            unfocusedBorderColor = InputBorder,
            disabledBorderColor = InputBorder.copy(alpha = 0.5f),
            focusedContainerColor = InputBackground,
            unfocusedContainerColor = InputBackground,
            disabledContainerColor = InputBackground,
            focusedTextColor = InputTextColor,
            unfocusedTextColor = InputTextColor,
            focusedLabelColor = GoldAura,
            unfocusedLabelColor = InputLabel,
            cursorColor = GoldAura
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0E8)
@Composable
private fun AuraTextFieldPreview() {
    AuraTextField(
        value = "",
        onValueChange = {},
        label = "E-MAIL ADDRESS",
        placeholder = "executive@atelier.com"
    )
}