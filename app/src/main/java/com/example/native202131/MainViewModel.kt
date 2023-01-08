package com.example.native202131

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.native202131.network.RepoModel
import com.example.native202131.network.UserModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

class MainViewModel : ViewModel() {
    private val logger by lazy { LoggerFactory.getLogger(javaClass) }
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    fun setMessage(text: String) {
        logger.info("setMessage $text")
        viewModelScope.launch {
            runCatching {
                val user = Fuel.get("https://api.github.com/users/google").awaitString()
                val userModel = json.decodeFromString<UserModel>(user)
                val repo = Fuel.get(userModel.reposUrl).awaitString()
                json.decodeFromString<List<RepoModel>>(repo)
            }.onSuccess {
                _message.value = it.size.toString()
            }.onFailure {
                logger.error("onClick", it)
                _message.value = it.localizedMessage ?: ""
            }
        }
    }
}
