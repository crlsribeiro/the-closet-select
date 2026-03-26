package com.carlosribeiro.theclosetselect.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.carlosribeiro.theclosetselect.presentation.components.AuraButton
import com.carlosribeiro.theclosetselect.presentation.components.AuraSecondaryButton

private val BackgroundColor = Color(0xFF0D0D0D)
private val CardBackground = Color(0xFF1A1A1A)
private val GoldColor = Color(0xFFB8972A)
private val SubtitleColor = Color(0xFF888888)
private val ObsidianColor = Color(0xFF1C1C1C)
private val AuraGoldColor = Color(0xFFD4A847)
private val SoftRoseColor = Color(0xFFE8A0A8)
private val EtherealColor = Color(0xFFD0D0D0)

data class ArchiveItem(
    val name: String,
    val category: String,
    val imageUrl: String
)

private val sampleArchiveItems = listOf(
    ArchiveItem(
        name = "Structured Wool Coat",
        category = "OUTERWEAR / WINTER",
        imageUrl = "https://images.unsplash.com/photo-1544022613-e87ca75a784a?w=400"
    ),
    ArchiveItem(
        name = "Silk Blouse",
        category = "ESSENTIALS",
        imageUrl = "https://images.unsplash.com/photo-1564257631407-4deb1f99d992?w=400"
    ),
    ArchiveItem(
        name = "Tailored Blazer",
        category = "FORMAL / WORK",
        imageUrl = "https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=400"
    )
)

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onNavigateToGerarLook: () -> Unit,
    onNavigateToDailyEnergy: () -> Unit,
    onNavigateToAuraPalette: () -> Unit,
    onNavigateToArchive: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HomeTopBar()

            Spacer(modifier = Modifier.height(16.dp))

            CuratorCard(onNavigateToGerarLook)

            Spacer(modifier = Modifier.height(24.dp))

            DailyEnergyCard(onNavigateToDailyEnergy)

            Spacer(modifier = Modifier.height(24.dp))

            AuraPaletteSection(onNavigateToAuraPalette)

            Spacer(modifier = Modifier.height(32.dp))

            ArchiveSection(
                onNavigateToArchive = onNavigateToArchive
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun HomeTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "THE CLOSET SELECT",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Light,
            letterSpacing = 3.sp
        )
    }
}

@Composable
private fun CuratorCard(onNavigateToGerarLook: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(340.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onNavigateToGerarLook() }
    ) {
        AsyncImage(
            model = "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=600",
            contentDescription = "Curador",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xCC000000)),
                        startY = 100f
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Text(
                text = "CURADOR",
                color = SubtitleColor,
                fontSize = 11.sp,
                letterSpacing = 2.sp
            )
            Text(
                text = "Gerar Look",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            AuraButton(
                text = "GERAR LOOK",
                onClick = onNavigateToGerarLook
            )
            Spacer(modifier = Modifier.height(10.dp))
            AuraSecondaryButton(
                text = "DETALHES",
                onClick = onNavigateToGerarLook
            )
        }
    }
}

@Composable
private fun DailyEnergyCard(onNavigateToDailyEnergy: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(CardBackground)
            .clickable { onNavigateToDailyEnergy() }
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Daily Energy",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "✦", color = GoldColor, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Seu estilo hoje reflete uma aura de autoridade e minimalismo. A combinação de texturas foscas está em alta para seu perfil.",
            color = SubtitleColor,
            fontSize = 13.sp,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "CONFIDENCE METER",
                color = GoldColor,
                fontSize = 10.sp,
                letterSpacing = 1.sp
            )
            Text(
                text = "88%",
                color = GoldColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFF2C2C2C))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(GoldColor)
            )
        }
    }
}

@Composable
private fun AuraPaletteSection(onNavigateToAuraPalette: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onNavigateToAuraPalette() }
    ) {
        Text(
            text = "AURA PALETTE",
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
            PaletteChip(color = ObsidianColor, label = "OBSIDIAN", modifier = Modifier.weight(1f))
            PaletteChip(color = AuraGoldColor, label = "AURA GOLD", modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaletteChip(color = SoftRoseColor, label = "SOFT ROSE", modifier = Modifier.weight(1f))
            PaletteChip(color = EtherealColor, label = "ETHEREAL", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun PaletteChip(color: Color, label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = label, color = SubtitleColor, fontSize = 10.sp, letterSpacing = 1.sp)
    }
}

@Composable
private fun ArchiveSection(onNavigateToArchive: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = "The Archive",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Peças selecionadas do seu closet",
                    color = SubtitleColor,
                    fontSize = 12.sp
                )
            }
            TextButton(onClick = onNavigateToArchive) {
                Text(
                    text = "VER TUDO",
                    color = GoldColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(sampleArchiveItems) { item ->
                ArchiveItemCard(
                    item = item,
                    onClick = onNavigateToArchive
                )
            }
        }
    }
}

@Composable
private fun ArchiveItemCard(item: ArchiveItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(180.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.85f)
                .clip(RoundedCornerShape(16.dp))
                .background(CardBackground)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.name,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = item.category,
            color = SubtitleColor,
            fontSize = 10.sp,
            letterSpacing = 1.sp
        )
    }
}