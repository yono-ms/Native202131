package com.example.native202131

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.native202131.database.RepoEntity
import com.example.native202131.ui.theme.Native202131Theme

@Composable
fun RepoScreen(repos: List<RepoEntity>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(repos) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = it.repoId.toString(), fontSize = 10.sp)
                    Text(text = it.updatedAt.fromIsoToDate().toBestString(), fontSize = 10.sp)
                }
                Text(text = it.name)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepoPreview() {
    Native202131Theme {
        val repos = listOf(
            RepoEntity(1, 99, 111111, "repo name 1", "2023"),
            RepoEntity(2, 99, 222222, "repo name 2", "2023"),
            RepoEntity(3, 99, 333333, "repo name 3", "2023"),
        )
        RepoScreen(repos)
    }
}
