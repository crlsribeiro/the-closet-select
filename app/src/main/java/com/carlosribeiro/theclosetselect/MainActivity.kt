package com.carlosribeiro.theclosetselect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.carlosribeiro.theclosetselect.presentation.navigation.NavGraph
import com.carlosribeiro.theclosetselect.presentation.theme.TheClosetSelectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheClosetSelectTheme {
                // O ERRO ESTAVA AQUI: Faltava o rememberNavController()
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}