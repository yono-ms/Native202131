package com.example.native202131

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.native202131.database.UserEntity
import com.example.native202131.ui.theme.Native202131Theme
import java.util.*

@Composable
fun UserScreen(
    login: String,
    users: List<UserEntity>,
    onSelect: (text: String) -> Unit,
    onInput: () -> Unit,
    onGet: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
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
        }, enabled = login.isNotEmpty()) {
            Text(text = "GET")
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            logger.info("onSelect ! ${it.login}")
                            onSelect(it.login)
                        }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = Date(it.cachedAt).toBestString(), fontSize = 10.sp)
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
            UserEntity(1, 1111111, "user login 1", 11, "user name 1", "url", 1, "2023"),
            UserEntity(2, 2222222, "user login 2", 12, "user name 2", "url", 2, "2023"),
            UserEntity(3, 3333333, "user login 3", 13, "user name 3", "url", 3, "2023"),
        )
        UserScreen("login", users, {}, {}, {})
    }
}
