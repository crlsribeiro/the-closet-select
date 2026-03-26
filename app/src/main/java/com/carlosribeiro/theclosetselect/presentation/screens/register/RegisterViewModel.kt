package com.carlosribeiro.theclosetselect.presentation.screens.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val birthDate: String = "",
    val zodiacSign: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isAgeValid: Boolean = true,
    val canSave: Boolean = false
)

class RegisterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(nv: String) = _uiState.update { it.copy(name = nv) }
    fun onEmailChange(nv: String) = _uiState.update { it.copy(email = nv) }
    fun onDateOfBirthChange(nv: String) = _uiState.update { it.copy(birthDate = nv) }
    fun onPasswordChange(nv: String) = _uiState.update { it.copy(password = nv) }
    fun onConfirmPasswordChange(nv: String) = _uiState.update { it.copy(confirmPassword = nv) }
    fun onRegisterClick() { /* Lógica Firebase */ }
}
// APAGUE QUALQUER COISA QUE ESTIVER ABAIXO DAQUI NESTE ARQUIVO