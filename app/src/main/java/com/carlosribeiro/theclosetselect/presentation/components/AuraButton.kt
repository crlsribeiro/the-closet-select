package com.carlosribeiro.theclosetselect.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.carlosribeiro.theclosetselect.presentation.theme.DeepBlack
import com.carlosribeiro.theclosetselect.presentation.theme.GoldAura

@Composable
fun AuraButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = GoldAura, contentColor = DeepBlack),
        shape = RoundedCornerShape(28.dp)
    ) {
        Text(text = text.uppercase(), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AuraSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true // Adicione o parâmetro enabled
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = GoldAura
        ),
        // Versão corrigida que aceita o parâmetro 'enabled'
        border = ButtonDefaults.outlinedButtonBorder(enabled)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Medium
        )
    }
}