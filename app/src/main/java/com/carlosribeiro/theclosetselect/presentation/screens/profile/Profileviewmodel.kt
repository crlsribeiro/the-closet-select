package com.carlosribeiro.theclosetselect.presentation.screens.profile

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class ProfileUiState(
    val birthdate: TextFieldValue = TextFieldValue(""),
    val displayName: String = "",
    val isLoading: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val errorMessage: String? = null
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val auth      = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    init {
        loadExistingProfile()
    }

    private fun loadExistingProfile() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val doc = firestore.collection("users").document(uid).get().await()
                val savedBirthdate = doc.getString("birthdate") ?: ""
                _uiState.update {
                    it.copy(
                        birthdate = TextFieldValue(
                            text      = savedBirthdate,
                            selection = TextRange(savedBirthdate.length)
                        ),
                        displayName = doc.getString("displayName")
                            ?: auth.currentUser?.displayName
                            ?: ""
                    )
                }
            } catch (_: Exception) {
                // Silencioso — campos ficam vazios, usuário preenche manualmente
            }
        }
    }

    /**
     * Aplica máscara DD/MM/YYYY mantendo o cursor na posição correta após
     * inserção automática das barras.
     */
    fun onBirthdateChange(input: TextFieldValue) {
        val digits = input.text.filter { it.isDigit() }.take(8)

        val masked = buildString {
            digits.forEachIndexed { index, char ->
                if (index == 2 || index == 4) append('/')
                append(char)
            }
        }

        val digitsBeforeCursor = input.text.take(input.selection.end).count { it.isDigit() }
        val extraSlashes = when {
            digitsBeforeCursor > 4 -> 2
            digitsBeforeCursor > 2 -> 1
            else                   -> 0
        }
        val newCursor = (digitsBeforeCursor + extraSlashes).coerceAtMost(masked.length)

        _uiState.update {
            it.copy(
                birthdate    = TextFieldValue(text = masked, selection = TextRange(newCursor)),
                errorMessage = null
            )
        }
    }

    fun onDisplayNameChange(value: String) {
        _uiState.update { it.copy(displayName = value, errorMessage = null) }
    }

    fun onSaveClick() {
        val uid           = auth.currentUser?.uid ?: return
        val state         = _uiState.value
        val birthdateText = state.birthdate.text

        if (birthdateText.length < 10) {
            _uiState.update { it.copy(errorMessage = "Informe a data completa (DD/MM/AAAA)") }
            return
        }

        // Calcula signo a partir da nova data antes de salvar
        val zodiacSign = getZodiacSignFromBirthdate(birthdateText)
        if (zodiacSign == null) {
            _uiState.update { it.copy(errorMessage = "Data de nascimento inválida.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                firestore.collection("users").document(uid)
                    .set(
                        mapOf(
                            "birthdate"   to birthdateText,
                            "displayName" to state.displayName,
                            "zodiacSign"  to zodiacSign       // ← sempre atualizado junto
                        ),
                        SetOptions.merge()
                    )
                    .await()
                _uiState.update { it.copy(isLoading = false, isSaveSuccess = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Erro ao salvar. Tente novamente.")
                }
            }
        }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }
}

// ── Utilitário: calcula signo a partir de "DD/MM/YYYY" ───────────────────────
// Retorna null se a data for inválida, para bloquear o save
private fun getZodiacSignFromBirthdate(birthdate: String): String? {
    return try {
        val parts = birthdate.split("/")
        if (parts.size != 3) return null
        val day   = parts[0].toInt()
        val month = parts[1].toInt()
        if (day < 1 || day > 31 || month < 1 || month > 12) return null
        when {
            (month == 3  && day >= 21) || (month == 4  && day <= 19) -> "Áries"
            (month == 4  && day >= 20) || (month == 5  && day <= 20) -> "Touro"
            (month == 5  && day >= 21) || (month == 6  && day <= 20) -> "Gêmeos"
            (month == 6  && day >= 21) || (month == 7  && day <= 22) -> "Câncer"
            (month == 7  && day >= 23) || (month == 8  && day <= 22) -> "Leão"
            (month == 8  && day >= 23) || (month == 9  && day <= 22) -> "Virgem"
            (month == 9  && day >= 23) || (month == 10 && day <= 22) -> "Libra"
            (month == 10 && day >= 23) || (month == 11 && day <= 21) -> "Escorpião"
            (month == 11 && day >= 22) || (month == 12 && day <= 21) -> "Sagitário"
            (month == 12 && day >= 22) || (month == 1  && day <= 19) -> "Capricórnio"
            (month == 1  && day >= 20) || (month == 2  && day <= 18) -> "Aquário"
            (month == 2  && day >= 19) || (month == 3  && day <= 20) -> "Peixes"
            else -> null
        }
    } catch (_: Exception) { null }
}