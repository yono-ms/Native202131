package com.example.native202131

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.native202131.ui.theme.Native202131Theme

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    val message = viewModel.message.collectAsState()
    val busy = viewModel.busy.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = message.value)
        if (busy.value) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        NavHost(navController = navController, startDestination = NavRoute.HOME.name) {
            composable(NavRoute.HOME.name) {
                HomeScreen { navController.navigate(NavRoute.USER.name) }
            }
            composable(NavRoute.USER.name) {
                UserScreen(viewModel) { navController.navigate(NavRoute.INPUT.name) }
            }
            composable(NavRoute.INPUT.name) {
                InputScreen(viewModel) { navController.popBackStack() }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    Native202131Theme {
        MainScreen()
    }
}
