package com.carlosribeiro.theclosetselect.presentation.screens.dailyenergy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private val BackgroundColor = Color(0xFF0D0D0D)
private val CardBackground = Color(0xFF1A1A1A)
private val GoldColor = Color(0xFFB8972A)
private val SubtitleColor = Color(0xFF888888)

data class ZodiacInfo(
    val name: String,
    val symbol: String,
    val color: Color,
    val element: String,
    val energy: String,
    val confidence: Float,
    val styleMessage: String,
    val colorPalette: List<Pair<Color, String>>
)

private val zodiacData = mapOf(
    "Áries" to ZodiacInfo(
        name = "Áries", symbol = "♈", color = Color(0xFFE53935),
        element = "FOGO", energy = "92%", confidence = 0.92f,
        styleMessage = "Sua energia hoje é intensa e poderosa. Aposte em peças estruturadas e cores vibrantes que transmitam liderança.",
        colorPalette = listOf(Color(0xFFE53935) to "VERMELHO", Color(0xFF1C1C1C) to "OBSIDIAN", Color(0xFFFFD700) to "DOURADO", Color(0xFFFFFFFF) to "BRANCO")
    ),
    "Touro" to ZodiacInfo(
        name = "Touro", symbol = "♉", color = Color(0xFF43A047),
        element = "TERRA", energy = "85%", confidence = 0.85f,
        styleMessage = "Sua aura irradia estabilidade e sofisticação. Tecidos naturais e tons terrosos são seus aliados hoje.",
        colorPalette = listOf(Color(0xFF43A047) to "VERDE", Color(0xFF8D6E63) to "TERRA", Color(0xFFD4A847) to "OURO", Color(0xFFF5F5F5) to "CREME")
    ),
    "Gêmeos" to ZodiacInfo(
        name = "Gêmeos", symbol = "♊", color = Color(0xFFFDD835),
        element = "AR", energy = "88%", confidence = 0.88f,
        styleMessage = "Sua energia é versátil e criativa hoje. Mix de estampas e sobreposições expressam sua dualidade única.",
        colorPalette = listOf(Color(0xFFFDD835) to "AMARELO", Color(0xFF1C1C1C) to "PRETO", Color(0xFFE8A0A8) to "ROSA", Color(0xFFD0D0D0) to "PRATA")
    ),
    "Câncer" to ZodiacInfo(
        name = "Câncer", symbol = "♋", color = Color(0xFF90CAF9),
        element = "ÁGUA", energy = "78%", confidence = 0.78f,
        styleMessage = "Sua intuição está em alta. Peças fluidas em tons lunares criam o equilíbrio perfeito para hoje.",
        colorPalette = listOf(Color(0xFF90CAF9) to "AZUL LUA", Color(0xFFE8EAF6) to "NÉVOA", Color(0xFFD4A847) to "PÉROLA", Color(0xFF1C1C1C) to "NOITE")
    ),
    "Leão" to ZodiacInfo(
        name = "Leão", symbol = "♌", color = Color(0xFFFF8F00),
        element = "FOGO", energy = "95%", confidence = 0.95f,
        styleMessage = "Você está no seu elemento hoje. Peças statement e dourados amplificam sua presença magnética.",
        colorPalette = listOf(Color(0xFFFF8F00) to "ÂMBAR", Color(0xFFD4A847) to "DOURADO", Color(0xFF1C1C1C) to "OBSIDIAN", Color(0xFFE53935) to "RUBI")
    ),
    "Virgem" to ZodiacInfo(
        name = "Virgem", symbol = "♍", color = Color(0xFF66BB6A),
        element = "TERRA", energy = "82%", confidence = 0.82f,
        styleMessage = "Sua precisão e elegância estão em sintonia. Minimalismo refinado e linhas limpas definem seu dia.",
        colorPalette = listOf(Color(0xFF66BB6A) to "SAGE", Color(0xFFF5F5F5) to "MARFIM", Color(0xFF8D6E63) to "NUDE", Color(0xFF1C1C1C) to "GRAFITE")
    ),
    "Libra" to ZodiacInfo(
        name = "Libra", symbol = "♎", color = Color(0xFFE8A0A8),
        element = "AR", energy = "87%", confidence = 0.87f,
        styleMessage = "Seu equilíbrio estético está afiado. Simetria, romantismo e harmonia de cores fazem sua assinatura hoje.",
        colorPalette = listOf(Color(0xFFE8A0A8) to "ROSE", Color(0xFFD0D0D0) to "PRATA", Color(0xFFFFFFFF) to "BRANCO", Color(0xFFD4A847) to "DOURADO")
    ),
    "Escorpião" to ZodiacInfo(
        name = "Escorpião", symbol = "♏", color = Color(0xFF7B1FA2),
        element = "ÁGUA", energy = "91%", confidence = 0.91f,
        styleMessage = "Sua intensidade é magnética hoje. Pretos profundos e bordôs transmitem seu poder misterioso.",
        colorPalette = listOf(Color(0xFF7B1FA2) to "PÚRPURA", Color(0xFF1C1C1C) to "OBSIDIAN", Color(0xFF880E4F) to "BORDÔ", Color(0xFFD4A847) to "BRONZE")
    ),
    "Sagitário" to ZodiacInfo(
        name = "Sagitário", symbol = "♐", color = Color(0xFF7E57C2),
        element = "FOGO", energy = "90%", confidence = 0.90f,
        styleMessage = "Sua energia aventureira está em alta. Peças com movimento e cores vibrantes refletem seu espírito livre.",
        colorPalette = listOf(Color(0xFF7E57C2) to "ÍNDIGO", Color(0xFFFF8F00) to "LARANJA", Color(0xFFD4A847) to "DOURADO", Color(0xFF1C1C1C) to "PRETO")
    ),
    "Capricórnio" to ZodiacInfo(
        name = "Capricórnio", symbol = "♑", color = Color(0xFF78909C),
        element = "TERRA", energy = "84%", confidence = 0.84f,
        styleMessage = "Sua autoridade natural irradia hoje. Alfaiataria impecável e neutros sofisticados são sua armadura.",
        colorPalette = listOf(Color(0xFF78909C) to "ARDÓSIA", Color(0xFF1C1C1C) to "CARVÃO", Color(0xFFD4A847) to "OURO", Color(0xFFF5F5F5) to "GELO")
    ),
    "Aquário" to ZodiacInfo(
        name = "Aquário", symbol = "♒", color = Color(0xFF29B6F6),
        element = "AR", energy = "89%", confidence = 0.89f,
        styleMessage = "Sua originalidade está no pico hoje. Misture o inesperado e crie combinações que ninguém mais ousaria.",
        colorPalette = listOf(Color(0xFF29B6F6) to "CIANO", Color(0xFF7E57C2) to "VIOLETA", Color(0xFF1C1C1C) to "PRETO", Color(0xFFD0D0D0) to "PRATA")
    ),
    "Peixes" to ZodiacInfo(
        name = "Peixes", symbol = "♓", color = Color(0xFF80DEEA),
        element = "ÁGUA", energy = "80%", confidence = 0.80f,
        styleMessage = "Sua sensibilidade é sua força hoje. Tecidos etéreos e tons aquáticos criam sua aura encantadora.",
        colorPalette = listOf(Color(0xFF80DEEA) to "AQUA", Color(0xFFE8A0A8) to "LAVANDA", Color(0xFFD0D0D0) to "PÉROLA", Color(0xFF1C1C1C) to "PROFUNDO")
    )
)

@Composable
fun DailyEnergyScreen() {
    var userSign by remember { mutableStateOf("Leão") }

    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    val sign = doc.getString("sign")
                    if (!sign.isNullOrBlank()) userSign = sign
                }
        }
    }

    val zodiac = zodiacData[userSign] ?: zodiacData["Leão"]!!

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DailyEnergyHeader(zodiac)

            Spacer(modifier = Modifier.height(32.dp))

            EnergyMessageCard(zodiac)

            Spacer(modifier = Modifier.height(24.dp))

            ConfidenceMeterCard(zodiac)

            Spacer(modifier = Modifier.height(24.dp))

            ColorPaletteCard(zodiac)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun DailyEnergyHeader(zodiac: ZodiacInfo) {
    Text(
        text = zodiac.symbol,
        color = zodiac.color,
        fontSize = 64.sp,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = zodiac.name.uppercase(),
        color = Color.White,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 4.sp,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = "ELEMENTO ${zodiac.element}",
        color = zodiac.color,
        fontSize = 11.sp,
        letterSpacing = 2.sp,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = "DAILY ENERGY",
        color = SubtitleColor,
        fontSize = 11.sp,
        letterSpacing = 3.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun EnergyMessageCard(zodiac: ZodiacInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardBackground)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mensagem do Dia",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "✦", color = zodiac.color, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = zodiac.styleMessage,
            color = SubtitleColor,
            fontSize = 13.sp,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun ConfidenceMeterCard(zodiac: ZodiacInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardBackground)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "CONFIDENCE METER",
                color = zodiac.color,
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = zodiac.energy,
                color = zodiac.color,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFF2C2C2C))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(zodiac.confidence)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(zodiac.color)
            )
        }
    }
}

@Composable
private fun ColorPaletteCard(zodiac: ZodiacInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardBackground)
            .padding(20.dp)
    ) {
        Text(
            text = "PALETA DO DIA",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            zodiac.colorPalette.take(2).forEach { (color, label) ->
                PaletteChip(color = color, label = label, modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            zodiac.colorPalette.drop(2).forEach { (color, label) ->
                PaletteChip(color = color, label = label, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun PaletteChip(color: Color, label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = SubtitleColor,
            fontSize = 9.sp,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
    }
}