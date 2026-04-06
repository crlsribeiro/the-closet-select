package com.carlosribeiro.theclosetselect.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.carlosribeiro.theclosetselect.presentation.screens.archive.ArchiveScreen
import com.carlosribeiro.theclosetselect.presentation.screens.aurapalette.AuraPaletteScreen
import com.carlosribeiro.theclosetselect.presentation.screens.dailyenergy.DailyEnergyScreen
import com.carlosribeiro.theclosetselect.presentation.screens.forgot_password.ForgotPasswordScreen
import com.carlosribeiro.theclosetselect.presentation.screens.gerarlook.GerarLookScreen
import com.carlosribeiro.theclosetselect.presentation.screens.home.HomeScreen
import com.carlosribeiro.theclosetselect.presentation.screens.login.LoginScreen
import com.carlosribeiro.theclosetselect.presentation.screens.register.RegisterScreen
import com.carlosribeiro.theclosetselect.presentation.screens.splash.SplashScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    val destination = if (FirebaseAuth.getInstance().currentUser != null) {
                        "home"
                    } else {
                        Screen.Login.route
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LaunchedEffect(Unit) {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    navController.navigate("home") {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToGerarLook = { navController.navigate("gerar_look") },
                onNavigateToDailyEnergy = { navController.navigate("daily_energy") },
                onNavigateToAuraPalette = { navController.navigate("aura_palette") },
                onNavigateToArchive = { navController.navigate("archive") }
            )
        }

        composable("gerar_look") {
            GerarLookScreen()
        }

        composable("daily_energy") {
            DailyEnergyScreen()
        }

        composable("aura_palette") {
            AuraPaletteScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("archive") {
            ArchiveScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}