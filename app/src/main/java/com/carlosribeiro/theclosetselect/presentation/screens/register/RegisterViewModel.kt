package com.carlosribeiro.theclosetselect.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosribeiro.theclosetselect.data.model.User
import com.carlosribeiro.theclosetselect.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado da UI para Registro.
 * Separado para garantir imutabilidade e facilidade de manutenção.
 */
data class RegisterState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val birthDate: String = "",
    val zodiacSign: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) {
    // Lógica computada para o botão Salvar (Evita lógica solta no ViewModel)
    val canSave: Boolean
        get() = firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                email.contains("@") &&
                password.length >= 6 &&
                password == confirmPassword &&
                birthDate.length == 10
}

class RegisterViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState = _uiState.asStateFlow()

    // Funções de atualização com nomes claros (SonarQube Friendly)
    fun onFirstNameChange(name: String) = _uiState.update { it.copy(firstName = name) }

    fun onLastNameChange(lastName: String) = _uiState.update { it.copy(lastName = lastName) }

    fun onEmailChange(email: String) = _uiState.update { it.copy(email = email) }

    fun onPasswordChange(password: String) = _uiState.update { it.copy(password = password) }

    fun onConfirmPasswordChange(password: String) = _uiState.update { it.copy(confirmPassword = password) }

    fun onDateOfBirthChange(date: String) {
        _uiState.update { it.copy(birthDate = date) }

        // Se a data estiver completa (Ex: 15/03/1990), calculamos o signo
        if (date.length == 10) {
            val sign = calculateZodiacSign(date)
            _uiState.update { it.copy(zodiacSign = sign) }
        }
    }

    /**
     * Lógica para o botão de registro.
     */
    fun onRegisterClick() {
        val state = _uiState.value

        if (!state.canSave) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val newUser = User(
                name = "${state.firstName} ${state.lastName}",
                email = state.email,
                birthDate = state.birthDate,
                zodiacSign = state.zodiacSign
            )

            val result = repository.signUpWithEmail(newUser, state.password)

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }

            result.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Erro ao cadastrar"
                    )
                }
            }
        }
    }

    /**
     * Função auxiliar para calcular o signo com base na string DD/MM/AAAA.
     * Implementação simplificada para o SonarQube.
     */
    private fun calculateZodiacSign(date: String): String {
        return try {
            val day = date.substring(0, 2).toInt()
            val month = date.substring(3, 5).toInt()

            when (month) {
                1 -> if (day < 20) "Capricórnio" else "Aquário"
                2 -> if (day < 19) "Aquário" else "Peixes"
                3 -> if (day < 21) "Peixes" else "Áries"
                4 -> if (day < 20) "Áries" else "Touro"
                5 -> if (day < 21) "Touro" else "Gêmeos"
                6 -> if (day < 21) "Gêmeos" else "Câncer"
                7 -> if (day < 23) "Câncer" else "Leão"
                8 -> if (day < 23) "Leão" else "Virgem"
                9 -> if (day < 23) "Virgem" else "Libra"
                10 -> if (day < 23) "Libra" else "Escorpião"
                11 -> if (day < 22) "Escorpião" else "Sagitário"
                12 -> if (day < 22) "Sagitário" else "Capricórnio"
                else -> ""
            }
        } catch (e: Exception) {
            ""
        }
    }
}