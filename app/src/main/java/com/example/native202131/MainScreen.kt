package com.example.native202131

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.native202131.ui.theme.Native202131Theme

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val message = viewModel.message.collectAsState()
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = message.value)
        Button(onClick = {
            logger.info("onClick !")
            viewModel.setMessage("Click")
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
