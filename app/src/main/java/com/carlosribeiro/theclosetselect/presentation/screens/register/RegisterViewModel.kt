package com.carlosribeiro.theclosetselect.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosribeiro.theclosetselect.data.model.User
import com.carlosribeiro.theclosetselect.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val birthDate: String = "",
    val zodiacSign: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isAgeValid: Boolean = true,
    val canSave: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class RegisterViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(nv: String) = _uiState.update { it.copy(name = nv) }
    fun onEmailChange(nv: String) = _uiState.update { it.copy(email = nv) }
    fun onDateOfBirthChange(nv: String) = _uiState.update { it.copy(birthDate = nv) }
    fun onPasswordChange(nv: String) = _uiState.update { it.copy(password = nv) }
    fun onConfirmPasswordChange(nv: String) = _uiState.update { it.copy(confirmPassword = nv) }

    fun onRegisterClick() {
        val state = _uiState.value

        // Basic validation check before calling Firebase
        if (state.email.isBlank() || state.password.isBlank() || state.password != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Passwords do not match or fields are empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val newUser = User(
                name = state.name,
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
                        errorMessage = error.localizedMessage ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }
}