package com.carlosribeiro.theclosetselect.presentation.screens.register

data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val birthDate: String = "", // Formato DD/MM/AAAA
    val zodiacSign: String = "", // Calculado automaticamente
    val password: String = "",
    val confirmPassword: String = "",
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isAgeValid: Boolean = true,
    val canSave: Boolean = false
)