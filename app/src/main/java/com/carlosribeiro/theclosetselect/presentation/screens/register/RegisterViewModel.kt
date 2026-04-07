package com.carlosribeiro.theclosetselect.presentation.screens.register

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosribeiro.theclosetselect.data.model.User
import com.carlosribeiro.theclosetselect.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

data class RegisterState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val birthDate: TextFieldValue = TextFieldValue(""),  // ← TextFieldValue para cursor correto
    val zodiacSign: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val isBrazilianLocale: Boolean = true
) {
    val dateHint: String get() = if (isBrazilianLocale) "DD/MM/AAAA" else "MM/DD/YYYY"

    val canSave: Boolean
        get() = firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                isEmailValid(email) &&
                password.length >= 6 &&
                password == confirmPassword &&
                birthDate.text.length == 10
}

private fun isEmailValid(email: String): Boolean {
    return email.contains("@") &&
            email.contains(".") &&
            email.indexOf("@") < email.lastIndexOf(".") &&
            !email.startsWith("@") &&
            !email.endsWith(".")
}

class RegisterViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        RegisterState(isBrazilianLocale = Locale.getDefault().language == "pt")
    )
    val uiState = _uiState.asStateFlow()

    fun onFirstNameChange(name: String) =
        _uiState.update { it.copy(firstName = name, errorMessage = null) }

    fun onLastNameChange(lastName: String) =
        _uiState.update { it.copy(lastName = lastName, errorMessage = null) }

    fun onEmailChange(email: String) =
        _uiState.update { it.copy(email = email, errorMessage = null) }

    fun onPasswordChange(password: String) =
        _uiState.update { it.copy(password = password, errorMessage = null) }

    fun onConfirmPasswordChange(password: String) =
        _uiState.update { it.copy(confirmPassword = password, errorMessage = null) }

    fun onDateOfBirthChange(input: TextFieldValue) {
        val digits = input.text.filter { it.isDigit() }.take(8)

        val masked = buildString {
            digits.forEachIndexed { index, char ->
                if (index == 2 || index == 4) append('/')
                append(char)
            }
        }

        // Recalcula cursor levando em conta as barras inseridas automaticamente
        val digitsBeforeCursor = input.text.take(input.selection.end).count { it.isDigit() }
        val extraSlashes = when {
            digitsBeforeCursor > 4 -> 2
            digitsBeforeCursor > 2 -> 1
            else                   -> 0
        }
        val newCursor = (digitsBeforeCursor + extraSlashes).coerceAtMost(masked.length)

        val newValue = TextFieldValue(text = masked, selection = TextRange(newCursor))

        val zodiacSign = if (masked.length == 10) {
            calculateZodiacSign(masked, _uiState.value.isBrazilianLocale)
        } else ""

        _uiState.update {
            it.copy(birthDate = newValue, zodiacSign = zodiacSign, errorMessage = null)
        }
    }

    fun onRegisterClick() {
        val state = _uiState.value

        if (!isEmailValid(state.email)) {
            _uiState.update { it.copy(errorMessage = "Por favor, insira um e-mail válido.") }
            return
        }

        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "As senhas não coincidem.") }
            return
        }

        if (state.password.length < 6) {
            _uiState.update { it.copy(errorMessage = "A senha deve ter pelo menos 6 caracteres.") }
            return
        }

        if (!state.canSave) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val newUser = User(
                displayName       = "${state.firstName} ${state.lastName}",
                email      = state.email,
                birthdate  = state.birthDate.text,   // ← era birthDate
                zodiacSign = state.zodiacSign
            )

            repository.signUpWithEmail(newUser, state.password)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading    = false,
                            errorMessage = error.localizedMessage ?: "Erro ao cadastrar. Tente novamente."
                        )
                    }
                }
        }
    }

    private fun calculateZodiacSign(date: String, isBrazilian: Boolean): String {
        return try {
            val parts = date.split("/")
            val day   = if (isBrazilian) parts[0].toInt() else parts[1].toInt()
            val month = if (isBrazilian) parts[1].toInt() else parts[0].toInt()
            when (month) {
                1  -> if (day < 20) "Capricórnio" else "Aquário"
                2  -> if (day < 19) "Aquário" else "Peixes"
                3  -> if (day < 21) "Peixes" else "Áries"
                4  -> if (day < 20) "Áries" else "Touro"
                5  -> if (day < 21) "Touro" else "Gêmeos"
                6  -> if (day < 21) "Gêmeos" else "Câncer"
                7  -> if (day < 23) "Câncer" else "Leão"
                8  -> if (day < 23) "Leão" else "Virgem"
                9  -> if (day < 23) "Virgem" else "Libra"
                10 -> if (day < 23) "Libra" else "Escorpião"
                11 -> if (day < 22) "Escorpião" else "Sagitário"
                12 -> if (day < 22) "Sagitário" else "Capricórnio"
                else -> ""
            }
        } catch (_: Exception) { "" }
    }
}