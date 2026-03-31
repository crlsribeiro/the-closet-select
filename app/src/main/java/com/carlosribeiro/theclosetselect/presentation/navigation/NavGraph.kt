package com.carlosribeiro.theclosetselect.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.carlosribeiro.theclosetselect.presentation.screens.archive.ArchiveScreen
import com.carlosribeiro.theclosetselect.presentation.screens.aurapalette.AuraPaletteScreen
import com.carlosribeiro.theclosetselect.presentation.screens.dailyenergy.DailyEnergyScreen
import com.carlosribeiro.theclosetselect.presentation.screens.forgotpassword.ForgotPasswordScreen
import com.carlosribeiro.theclosetselect.presentation.screens.gerarlook.GerarLookScreen
import com.carlosribeiro.theclosetselect.presentation.screens.home.HomeScreen
import com.carlosribeiro.theclosetselect.presentation.screens.login.LoginScreen
import com.carlosribeiro.theclosetselect.presentation.screens.register.RegisterScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(navController: NavHostController) {
    val startRoute = if (FirebaseAuth.getInstance().currentUser != null) "home" else "login"

    NavHost(navController = navController, startDestination = startRoute) {

        composable("login") {
            LaunchedEffect(Unit) {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToForgotPassword = {
                    navController.navigate("forgot_password")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToGerarLook = {
                    navController.navigate("gerar_look")
                },
                onNavigateToDailyEnergy = {
                    navController.navigate("daily_energy")
                },
                onNavigateToAuraPalette = {
                    navController.navigate("aura_palette")
                },
                onNavigateToArchive = {
                    navController.navigate("archive")
                }
            )
        }

        composable("gerar_look") {
            GerarLookScreen()
        }

        composable("daily_energy") {
            DailyEnergyScreen()
        }

        composable("aura_palette") {
            AuraPaletteScreen()
        }

        composable("archive") {
            ArchiveScreen()
        }
    }
}