package com.example.native202131

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.native202131.database.RepoEntity
import com.example.native202131.database.UserEntity
import com.example.native202131.network.RepoModel
import com.example.native202131.network.UserModel
import com.example.native202131.network.toEntity
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

    private val _users = MutableStateFlow(listOf<UserEntity>())
    val users: StateFlow<List<UserEntity>> = _users

    private val _repos = MutableStateFlow(listOf<RepoEntity>())
    val repos: StateFlow<List<RepoEntity>> = _repos

    fun setMessage(text: String) {
        logger.info("setMessage $text")
        viewModelScope.launch {
            runCatching {
                val user = Fuel.get("https://api.github.com/users/google").awaitString()
                logger.trace("Fuel users DONE.")
                val userModel = json.decodeFromString<UserModel>(user)
                logger.trace("Json decode users DONE.")
                userDao.insert(userModel.toEntity())
                logger.trace("Room insert users DONE.")
                val repo = Fuel.get(userModel.reposUrl).awaitString()
                logger.trace("Fuel repos DONE.")
                val repoModel = json.decodeFromString<List<RepoModel>>(repo)
                logger.trace("Json decode repos DONE.")
                repoModel.forEach {
                    repoDao.insert(it.toEntity(userModel.id))
                }
            }.onSuccess {
                logger.trace("Room insert repos DONE.")
                _message.value = "Success."
            }.onFailure {
                logger.error("onClick", it)
                _message.value = it.localizedMessage ?: ""
            }
        }
    }

    init {
        viewModelScope.launch {
            runCatching {
                userDao.loadAllUser().collect {
                    logger.info("userDao collect ${it.size}")
                    _users.value = it
                }
            }.onFailure {
                logger.error("userDao collect", it)
            }
        }
        viewModelScope.launch {
            runCatching {
                repoDao.loadAllRepo().collect {
                    logger.info("repoDao collect ${it.size}")
                    _repos.value = it
                }
            }.onFailure {
                logger.error("repoDao collect", it)
            }
        }
    }
}
