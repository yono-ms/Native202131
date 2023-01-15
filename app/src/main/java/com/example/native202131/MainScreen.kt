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
    var login by rememberSaveable { mutableStateOf("") }
    val users = userDao.loadAllUser().collectAsState(initial = emptyList())
    var draftLogin by rememberSaveable { mutableStateOf("") }
    val repos = repoDao.loadAllRepo().collectAsState(initial = emptyList())
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = message.value)
        if (busy.value) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        NavHost(navController = navController, startDestination = NavRoute.HOME.name) {
            composable(NavRoute.HOME.name) {
                logger.trace("compose ${NavRoute.HOME.name}")
                HomeScreen { navController.navigate(it.name) }
            }
            composable(NavRoute.USER.name) {
                logger.trace("compose ${NavRoute.USER.name}")
                UserScreen(login = login, users = users.value, onSelect = {
                    login = it
                }, onInput = {
                    navController.navigate(NavRoute.INPUT.name)
                    draftLogin = login
                }, onGet = {
                    navController.navigate(NavRoute.REPO.name)
                    viewModel.onGet(login)
                })
            }
            composable(NavRoute.INPUT.name) {
                logger.trace("compose ${NavRoute.INPUT.name}")
                InputScreen(draftLogin = draftLogin, onChange = {
                    draftLogin = it
                }, onDone = {
                    navController.popBackStack()
                    login = draftLogin
                })
            }
            composable(NavRoute.REPO.name) {
                logger.trace("compose ${NavRoute.REPO.name}")
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
