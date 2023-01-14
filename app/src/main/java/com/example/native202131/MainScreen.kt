package com.example.native202131

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
    val login = viewModel.login.collectAsState()
    val users = userDao.loadAllUser().collectAsState(initial = emptyList())
    var draftLogon by rememberSaveable { mutableStateOf("") }
    val repos = repoDao.loadAllRepo().collectAsState(initial = emptyList())
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = message.value)
        if (busy.value) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        NavHost(navController = navController, startDestination = NavRoute.HOME.name) {
            composable(NavRoute.HOME.name) {
                HomeScreen { navController.navigate(it.name) }
            }
            composable(NavRoute.USER.name) {
                UserScreen(login = login.value, users = users.value, onSelect = {
                    viewModel.updateLogin(it)
                }, onInput = {
                    navController.navigate(NavRoute.INPUT.name)
                    draftLogon = login.value
                }, onGet = {
                    navController.navigate(NavRoute.REPO.name)
                    viewModel.onGet()
                })
            }
            composable(NavRoute.INPUT.name) {
                InputScreen(draftLogon = draftLogon, onChange = {
                    draftLogon = it
                }, onDone = {
                    navController.popBackStack()
                    viewModel.updateLogin(draftLogon)
                })
            }
            composable(NavRoute.REPO.name) {
                RepoScreen(repos.value)
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
