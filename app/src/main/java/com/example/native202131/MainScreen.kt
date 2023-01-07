package com.example.native202131

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.native202131.ui.theme.Native202131Theme

@Composable
fun MainScreen() {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Hello ${javaClass.simpleName}!")
        Button(onClick = {
            logger.info("onClick !")
        }) {
            Text(text = "LOG")
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
