package com.carlosribeiro.theclosetselect.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.carlosribeiro.theclosetselect.presentation.theme.GoldAura

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuraTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true, // Adicionado para resolver o erro 'enabled'
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        label = {
            Text(
                text = label.uppercase(),
                fontSize = 10.sp,
                letterSpacing = 1.sp
            )
        },
        modifier = modifier.fillMaxWidth(),
        visualTransformation = visualTransformation,
        textStyle = TextStyle(color = if (enabled) Color.White else Color.Gray, fontSize = 16.sp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GoldAura,
            unfocusedBorderColor = Color.DarkGray,
            disabledBorderColor = Color.DarkGray, // Para o campo de Signo
            focusedLabelColor = GoldAura,
            unfocusedLabelColor = Color.Gray,
            disabledLabelColor = Color.DarkGray,
            cursorColor = GoldAura
        )
    )
}