package com.carlosribeiro.theclosetselect.presentation.screens.forgot_password

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ForgotPasswordViewModel : ViewModel() {
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    fun onEmailChange(newValue: String) {
        _email.value = newValue
    }

    fun onSendClick() {
        // Futura integração com Firebase Auth para reset de senha
    }
}