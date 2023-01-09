package com.example.native202131

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.native202131.database.UserEntity
import com.example.native202131.ui.theme.Native202131Theme

@Composable
fun UserScreen(viewModel: MainViewModel = viewModel(), onNavigate: () -> Unit) {
    val login = viewModel.login.collectAsState()
    val users = viewModel.users.collectAsState()
    UserContent(login.value, users.value, {
        viewModel.updateDraftLogin(login.value)
        onNavigate()
    }) { viewModel.onGet() }
}

@Composable
fun UserContent(login: String, users: List<UserEntity>, onInput: () -> Unit, onGet: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                logger.info("onClick Input !")
                onInput()
            }) {
            Text(text = login)
        }
        Button(onClick = {
            logger.info("onClick GET !")
            onGet()
        }) {
            Text(text = "GET")
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) {
                Column(Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = it.loginId.toString(), fontSize = 10.sp)
                        Text(text = it.login, fontSize = 10.sp)
                    }
                    Text(text = it.name)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserPreview() {
    Native202131Theme {
        val users = listOf(
            UserEntity(1, 0, "user login 1", 11, "user name 1", "url", "2023"),
            UserEntity(2, 0, "user login 2", 12, "user name 2", "url", "2023"),
            UserEntity(3, 0, "user login 3", 13, "user name 3", "url", "2023"),
        )
        UserContent("login", users, {}, {})
    }
}
