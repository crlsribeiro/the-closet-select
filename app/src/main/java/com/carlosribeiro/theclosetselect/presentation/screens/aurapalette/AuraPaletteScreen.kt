package com.carlosribeiro.theclosetselect.presentation.screens.aurapalette

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carlosribeiro.theclosetselect.domain.model.PaletteColor
import com.carlosribeiro.theclosetselect.domain.model.PaletteResult

private val BackgroundDark  = Color(0xFF0D0D0D)
private val CardDark        = Color(0xFF1A1A1A)
private val Gold            = Color(0xFFB8972A)
private val TextMuted       = Color(0xFF888888)
private val BorderDark      = Color(0xFF2C2C2C)

private val quizQuestions = listOf(
    QuizQuestion(
        question = "Qual é a cor dos seus olhos?",
        options = listOf("Castanho escuro", "Castanho claro", "Verde", "Azul", "Cinza")
    ),
    QuizQuestion(
        question = "Qual é a cor natural do seu cabelo?",
        options = listOf("Preto", "Castanho escuro", "Castanho claro", "Loiro", "Ruivo")
    ),
    QuizQuestion(
        question = "Como você descreveria seu tom de pele?",
        options = listOf("Muito claro", "Claro", "Médio", "Moreno", "Escuro")
    ),
    QuizQuestion(
        question = "Qual é a cor das veias do seu pulso?",
        options = listOf("Azul ou roxo", "Verde", "Azul e verde", "Difícil de ver")
    ),
    QuizQuestion(
        question = "Quais cores costumam te render mais elogios?",
        options = listOf("Tons terrosos e quentes", "Tons frios e pastéis", "Cores vibrantes", "Neutros como preto e branco")
    ),
    QuizQuestion(
        question = "Quando você usa branco puro perto do rosto, o efeito é:",
        options = listOf("Me deixa com aparência cansada", "Me ilumina o rosto", "Me deixa pálida", "Não percebo diferença")
    ),
    QuizQuestion(
        question = "Qual tipo de joia combina mais com você?",
        options = listOf("Ouro amarelo", "Prata ou ouro branco", "Rose gold", "Qualquer um fica bem")
    ),
    QuizQuestion(
        question = "Como sua pele reage ao sol?",
        options = listOf("Bronzeia facilmente e fica dourada", "Fica rosada antes de bronzear", "Quase nunca bronzeia, fica vermelha", "Bronzeia rápido e fica bem escura")
    )
)

data class QuizQuestion(
    val question: String,
    val options: List<String>
)

@Composable
fun AuraPaletteScreen(onNavigateBack: () -> Unit = {}) {
    val viewModel: AuraPaletteViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    when (uiState.step) {
        AuraPaletteStep.CHECKING -> LoadingScreen()

        AuraPaletteStep.WELCOME -> WelcomeScreen(
            onStart = viewModel::onStartQuiz,
            onBack = onNavigateBack
        )

        AuraPaletteStep.QUIZ -> QuizScreen(
            currentQuestion = uiState.currentQuestion,
            totalQuestions = AuraPaletteViewModel.TOTAL_QUESTIONS,
            onAnswer = { answer -> viewModel.onAnswerQuestion(uiState.currentQuestion, answer) },
            onBack = onNavigateBack
        )

        AuraPaletteStep.ANALYZING -> AnalyzingScreen()

        AuraPaletteStep.RESULT -> ResultScreen(
            result = uiState.result,
            onBack = onNavigateBack,
            onRedo = viewModel::onRetryAnalysis
        )
    }

    uiState.errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = viewModel::onDismissError,
            title = { Text("Atenção", color = Color.White) },
            text = { Text(message, color = TextMuted) },
            confirmButton = {
                TextButton(onClick = viewModel::onDismissError) {
                    Text("OK", color = Gold)
                }
            },
            containerColor = CardDark
        )
    }
}

// ── Loading ───────────────────────────────────────────────────────────────────
@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Gold)
    }
}

// ── Welcome ───────────────────────────────────────────────────────────────────
@Composable
private fun WelcomeScreen(onStart: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "✦", color = Gold, fontSize = 48.sp, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "AURA PALETTE",
            color = Gold,
            fontSize = 12.sp,
            letterSpacing = 4.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Vamos descobrir\njuntas seu tom de cor",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Responda 8 perguntas rápidas e descubra sua paleta de cores ideal — as cores que realçam sua beleza natural.",
            color = TextMuted,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Gold)
        ) {
            Text("DESCOBRIR MINHA PALETA", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBack) {
            Text("Agora não", color = TextMuted, fontSize = 13.sp)
        }
    }
}

// ── Quiz ──────────────────────────────────────────────────────────────────────
@Composable
private fun QuizScreen(
    currentQuestion: Int,
    totalQuestions: Int,
    onAnswer: (String) -> Unit,
    onBack: () -> Unit
) {
    val question = quizQuestions[currentQuestion]
    val progress = (currentQuestion + 1).toFloat() / totalQuestions.toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "${currentQuestion + 1} de $totalQuestions",
                color = TextMuted,
                fontSize = 13.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(BorderDark)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(2.dp)
                    .background(Gold)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Pergunta
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "✦", color = Gold, fontSize = 24.sp)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = question.question,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            question.options.forEach { option ->
                OptionButton(
                    text = option,
                    onClick = { onAnswer(option) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun OptionButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, BorderDark, RoundedCornerShape(12.dp))
            .background(CardDark)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = text, color = Color.White, fontSize = 15.sp)
    }
}

// ── Analyzing ─────────────────────────────────────────────────────────────────
@Composable
private fun AnalyzingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = Gold, modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Analisando sua paleta...", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Isso pode levar alguns segundos", color = TextMuted, fontSize = 13.sp, fontStyle = FontStyle.Italic)
    }
}

// ── Result ────────────────────────────────────────────────────────────────────
@Composable
private fun ResultScreen(
    result: PaletteResult?,
    onBack: () -> Unit,
    onRedo: () -> Unit
) {
    if (result == null) {
        LoadingScreen()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "AURA PALETTE",
                color = Gold,
                fontSize = 12.sp,
                letterSpacing = 3.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Estação
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "✦", color = Gold, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = result.seasonType.uppercase(),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "SUBTOM ${result.subtom.uppercase()}", color = Gold, fontSize = 11.sp, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = result.descricao,
                color = TextMuted,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Melhores cores
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(CardDark)
                .padding(20.dp)
        ) {
            Text(text = "SUAS MELHORES CORES", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(16.dp))
            val chunked = result.bestColors.chunked(2)
            chunked.forEach { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    row.forEach { color ->
                        PaletteColorChip(paletteColor = color, modifier = Modifier.weight(1f))
                    }
                    if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Cores a evitar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(CardDark)
                .padding(20.dp)
        ) {
            Text(text = "CORES A EVITAR", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(16.dp))
            val chunked = result.colorsToAvoid.chunked(2)
            chunked.forEach { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    row.forEach { color ->
                        PaletteColorChip(paletteColor = color, modifier = Modifier.weight(1f))
                    }
                    if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        TextButton(
            onClick = onRedo,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Refazer análise", color = TextMuted, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun PaletteColorChip(paletteColor: PaletteColor, modifier: Modifier = Modifier) {
    val color = try {
        Color(android.graphics.Color.parseColor(paletteColor.hex))
    } catch (e: Exception) {
        Color(0xFF888888)
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = paletteColor.nome,
            color = TextMuted,
            fontSize = 9.sp,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}