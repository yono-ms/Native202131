package com.example.native202131

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.native202131.ui.theme.Native202131Theme

@Composable
fun InputScreen(viewModel: MainViewModel = viewModel(), onPopBackStack: () -> Unit) {
    val draftLogon = viewModel.draftLogin.collectAsState()
    InputContent(draftLogon.value, { viewModel.updateDraftLogin(it) }) {
        onPopBackStack()
        viewModel.updateLogin(draftLogon.value)
    }
}

const val LOGIN_MAX_LENGTH = 32

@Composable
fun InputContent(draftLogon: String, onChange: (text: String) -> Unit, onDone: () -> Unit) {
    TextField(
        value = draftLogon,
        onValueChange = {
            when {
                it.contains("¥n") -> logger.warn("contains RETURN")
                it.length > LOGIN_MAX_LENGTH -> logger.warn("length ${it.length} ($LOGIN_MAX_LENGTH)")
                else -> onChange(it)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            logger.info("onDone !")
            onDone()
        }),
        singleLine = true,
        maxLines = 1
    )
}

@Preview(showBackground = true)
@Composable
fun InputPreview() {
    Native202131Theme {
        InputContent("test", {}) {}
    }
}
