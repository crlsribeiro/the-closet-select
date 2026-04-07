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
import com.carlosribeiro.theclosetselect.presentation.screens.profile.ProfileScreen
import com.carlosribeiro.theclosetselect.presentation.screens.register.RegisterScreen
import com.carlosribeiro.theclosetselect.presentation.screens.splash.SplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController    = navController,
        startDestination = Screen.Splash.route
    ) {

        // ── Splash ────────────────────────────────────────────────────────────
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user == null) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(user.uid)
                            .get()
                            .addOnSuccessListener { doc ->
                                val destination = if (doc.getString("birthdate") != null)
                                    Screen.Home.route else Screen.Profile.route
                                navController.navigate(destination) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                            .addOnFailureListener {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                    }
                }
            )
        }

        // ── Login ─────────────────────────────────────────────────────────────
        composable(Screen.Login.route) {
            LaunchedEffect(Unit) {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            LoginScreen(
                onNavigateToRegister       = { navController.navigate(Screen.Register.route) },
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                onNavigateToHome           = {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if (uid == null) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(uid)
                            .get()
                            .addOnSuccessListener { doc ->
                                val destination = if (doc.getString("birthdate") != null)
                                    Screen.Home.route else Screen.Profile.route
                                navController.navigate(destination) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                            .addOnFailureListener {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                    }
                }
            )
        }

        // ── Register — já coleta nome + data, vai direto para Home ────────────
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToHome  = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // ── Forgot Password ───────────────────────────────────────────────────
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

        // ── Home ──────────────────────────────────────────────────────────────
        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToGerarLook   = { navController.navigate(Screen.GerarLook.route) },
                onNavigateToDailyEnergy = { navController.navigate(Screen.DailyEnergy.route) },
                onNavigateToAuraPalette = { navController.navigate(Screen.AuraPalette.route) },
                onNavigateToArchive     = { navController.navigate(Screen.Archive.route) },
                onNavigateToProfile     = { navController.navigate(Screen.Profile.route) }
            )
        }

        // ── Profile — SSO sem birthdate ou edição via card na Home ────────────
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // ── Demais telas ──────────────────────────────────────────────────────
        composable(Screen.GerarLook.route) {
            GerarLookScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.DailyEnergy.route) {
            DailyEnergyScreen()
        }

        composable(Screen.AuraPalette.route) {
            AuraPaletteScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.Archive.route) {
            ArchiveScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}