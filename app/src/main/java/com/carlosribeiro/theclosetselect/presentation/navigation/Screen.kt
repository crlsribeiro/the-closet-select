package com.carlosribeiro.theclosetselect.presentation.navigation

sealed class Screen(val route: String) {
    object Splash        : Screen("splash")
    object Login         : Screen("login")
    object Register      : Screen("register")
    object ForgotPassword: Screen("forgot_password")
    object Home          : Screen("home")
    object Profile       : Screen("profile")
    object GerarLook     : Screen("gerar_look")
    object DailyEnergy   : Screen("daily_energy")
    object AuraPalette   : Screen("aura_palette")
    object Archive       : Screen("archive")
}