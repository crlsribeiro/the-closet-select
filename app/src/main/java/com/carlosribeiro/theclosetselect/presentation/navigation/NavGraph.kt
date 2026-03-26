package com.carlosribeiro.theclosetselect.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carlosribeiro.theclosetselect.presentation.screens.login.LoginScreen
import com.carlosribeiro.theclosetselect.presentation.screens.login.LoginViewModel
import com.carlosribeiro.theclosetselect.presentation.screens.register.RegisterScreen
import com.carlosribeiro.theclosetselect.presentation.screens.register.RegisterViewModel
import com.carlosribeiro.theclosetselect.presentation.screens.forgot_password.ForgotPasswordScreen
import com.carlosribeiro.theclosetselect.presentation.screens.forgot_password.ForgotPasswordViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // Tela de Login
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel()
            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        // Tela de Registro
        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = viewModel()
            RegisterScreen(
                viewModel = registerViewModel,
                onBackClick = { // Agora o parâmetro 'onBackClick' existe na RegisterScreen que te passei
                    navController.popBackStack()
                }
            )
        }

        // Tela de Esqueci Senha
        composable(Screen.ForgotPassword.route) {
            val forgotPasswordViewModel: ForgotPasswordViewModel = viewModel()
            ForgotPasswordScreen(
                viewModel = forgotPasswordViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}