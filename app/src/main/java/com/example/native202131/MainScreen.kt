package com.example.native202131

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavRoute.HOME.name) {
        composable(NavRoute.HOME.name) {
            HomeScreen()
        }
    }
}
