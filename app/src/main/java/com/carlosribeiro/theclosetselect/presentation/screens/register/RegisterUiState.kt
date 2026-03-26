package com.carlosribeiro.theclosetselect.presentation.screens.register

/**
 * Estado da UI para a tela de Registro.
 * Seguindo padrões do SonarQube: Imutabilidade e clareza de tipos.
 */
data class RegisterUiState(
    // Dados de entrada do utilizador
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val birthDate: String = "", // Sugestão: Manter String para a máscara do campo (DD/MM/AAAA)
    val zodiacSign: String = "", // Será atualizado pelo ViewModel ao digitar a data
    val password: String = "",
    val confirmPassword: String = "",

    // Estados de Validação (O SonarQube prefere estados claros)
    val emailError: String? = null,    // Se null, está válido. Se tiver texto, é o erro.
    val passwordError: String? = null,
    val birthDateError: String? = null,

    // Controlo de UI
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    /**
     * Propriedade computada para habilitar o botão Salvar.
     * Centraliza a lógica de ativação do botão num único lugar.
     */
    val canSave: Boolean
        get() = firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                email.isNotBlank() &&
                emailError == null &&
                password.isNotBlank() &&
                passwordError == null &&
                password == confirmPassword &&
                birthDate.length == 10 && // Garante que a data está completa (DD/MM/AAAA)
                birthDateError == null
}