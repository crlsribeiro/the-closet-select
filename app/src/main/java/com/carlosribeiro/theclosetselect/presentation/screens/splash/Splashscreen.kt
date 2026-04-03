package com.carlosribeiro.theclosetselect.presentation.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlosribeiro.theclosetselect.R
import kotlinx.coroutines.delay

private val CardBackground = Color(0xB8C8C5BF)
private val TextDark       = Color(0xFF2B2B2B)
private val TextSubtitle   = Color(0xFF4A4A4A)

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {

    val screenAlpha = remember { Animatable(0f) }
    val cardAlpha   = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        screenAlpha.animateTo(1f, animationSpec = tween(800, easing = EaseIn))
        cardAlpha.animateTo(1f,   animationSpec = tween(600, easing = EaseOutCubic))
        delay(2000)
        cardAlpha.animateTo(0f,   animationSpec = tween(500))
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(screenAlpha.value)
    ) {

        Image(
            painter = painterResource(id = R.drawable.bg_closet),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x33000000))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .aspectRatio(0.85f)
                .align(Alignment.Center)
                .alpha(cardAlpha.value)
                .background(CardBackground)
                .padding(horizontal = 32.dp, vertical = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "WELCOME BACK",
                    color = TextSubtitle,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "THE\nCLOSET\nSELECT",
                    color = TextDark,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    lineHeight = 54.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}