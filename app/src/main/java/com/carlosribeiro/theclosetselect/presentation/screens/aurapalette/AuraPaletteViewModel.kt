package com.carlosribeiro.theclosetselect.presentation.screens.aurapalette

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosribeiro.theclosetselect.data.remote.PaletteAnalyzerService
import com.carlosribeiro.theclosetselect.data.remote.QuizAnswers
import com.carlosribeiro.theclosetselect.data.repository.PaletteRepositoryImpl
import com.carlosribeiro.theclosetselect.domain.model.PaletteResult
import com.carlosribeiro.theclosetselect.domain.usecase.GetPaletteUseCase
import com.carlosribeiro.theclosetselect.domain.usecase.SavePaletteUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.onSuccess

data class AuraPaletteState(
    val step: AuraPaletteStep = AuraPaletteStep.CHECKING,
    val currentQuestion: Int = 0,
    val quizAnswers: QuizAnswers = QuizAnswers(),
    val result: PaletteResult? = null,
    val isAnalyzing: Boolean = false,
    val errorMessage: String? = null
)

enum class AuraPaletteStep {
    CHECKING,
    WELCOME,
    QUIZ,
    ANALYZING,
    RESULT
}

class AuraPaletteViewModel : ViewModel() {

    private val paletteRepository = PaletteRepositoryImpl()
    private val analyzerService = PaletteAnalyzerService()
    private val getPaletteUseCase = GetPaletteUseCase(paletteRepository)
    private val savePaletteUseCase = SavePaletteUseCase(paletteRepository)

    private val _uiState = MutableStateFlow(AuraPaletteState())
    val uiState: StateFlow<AuraPaletteState> = _uiState.asStateFlow()

    private val userId: String = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    init {
        checkExistingPalette()
    }

    private fun checkExistingPalette() {
        viewModelScope.launch {
            val existing = getPaletteUseCase(userId)
            if (existing != null) {
                _uiState.update { it.copy(step = AuraPaletteStep.RESULT, result = existing) }
            } else {
                _uiState.update { it.copy(step = AuraPaletteStep.WELCOME) }
            }
        }
    }

    fun onStartQuiz() =
        _uiState.update { it.copy(step = AuraPaletteStep.QUIZ, currentQuestion = 0) }

    fun onAnswerQuestion(questionIndex: Int, answer: String) {
        val current = _uiState.value.quizAnswers
        val updated = when (questionIndex) {
            0 -> current.copy(eyeColor = answer)
            1 -> current.copy(hairColor = answer)
            2 -> current.copy(skinTone = answer)
            3 -> current.copy(veinColor = answer)
            4 -> current.copy(flatteringColors = answer)
            5 -> current.copy(whiteEffect = answer)
            6 -> current.copy(jewelryPreference = answer)
            7 -> current.copy(sunReaction = answer)
            else -> current
        }

        val nextQuestion = questionIndex + 1
        if (nextQuestion >= TOTAL_QUESTIONS) {
            _uiState.update { it.copy(quizAnswers = updated, step = AuraPaletteStep.ANALYZING) }
            analyzeWithClaude(updated)
        } else {
            _uiState.update { it.copy(quizAnswers = updated, currentQuestion = nextQuestion) }
        }
    }

    fun onRetryAnalysis() =
        _uiState.update {
            it.copy(
                step = AuraPaletteStep.WELCOME,
                quizAnswers = QuizAnswers(),
                currentQuestion = 0,
                errorMessage = null
            )
        }

    fun onDismissError() =
        _uiState.update { it.copy(errorMessage = null) }

    private fun analyzeWithClaude(quiz: QuizAnswers) {
        viewModelScope.launch {
            analyzerService.analyzePalette(quiz)
                .onSuccess { result: PaletteResult ->
                    val withUserId = result.copy(userId = userId)
                    savePaletteUseCase(withUserId)
                    _uiState.update { it.copy(step = AuraPaletteStep.RESULT, result = withUserId) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            step = AuraPaletteStep.QUIZ,
                            errorMessage = error.message ?: "Erro ao analisar. Tente novamente."
                        )
                    }
                }
        }
    }

    companion object {
        const val TOTAL_QUESTIONS = 8
    }
}