package com.example.native202131

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.native202131.ui.theme.Native202131Theme

@Composable
fun HomeScreen(onClick: (navRoute: NavRoute) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = {
            logger.info("onClick !")
            onClick(NavRoute.USER)
        }) {
            Text(text = "USER")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Native202131Theme {
        HomeScreen {}
    }
}
