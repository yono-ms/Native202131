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

    private val _busy = MutableStateFlow(false)
    val busy: StateFlow<Boolean> = _busy

    private val _users = MutableStateFlow(listOf<UserEntity>())
    val users: StateFlow<List<UserEntity>> = _users

    private val _repos = MutableStateFlow(listOf<RepoEntity>())
    val repos: StateFlow<List<RepoEntity>> = _repos

    private val _login = MutableStateFlow("apple")
    val login: StateFlow<String> = _login

    private val _draftLogin = MutableStateFlow("")
    val draftLogin: StateFlow<String> = _draftLogin

    fun updateLogin(text: String) {
        logger.info("updateLogin")
        _login.value = text
    }

    fun updateDraftLogin(text: String) {
        logger.info("updateDraftLogin")
        _draftLogin.value = text
    }

    fun onGet() {
        logger.info("onGet")
        viewModelScope.launch {
            runCatching {
                _busy.value = true
                _message.value = ""
                val user = Fuel.get("https://api.github.com/users/${_login.value}").awaitString()
                logger.trace("Fuel users DONE.")
                val userModel = json.decodeFromString<UserModel>(user)
                logger.trace("Json decode users DONE.")
                userDao.insert(userModel.toEntity())
                logger.trace("Room insert users DONE.")
                val repo = Fuel.get(userModel.reposUrl).awaitString()
                logger.trace("Fuel repos DONE.")
                val repoModels = json.decodeFromString<List<RepoModel>>(repo)
                logger.trace("Json decode repos DONE.")
                val list = repoModels.toEntity(userModel.id)
                repoDao.insertAll(*list.toTypedArray())
            }.onSuccess {
                logger.trace("Room insert repos DONE.")
            }.onFailure {
                logger.error("onClick", it)
                _message.value = it.localizedMessage ?: ""
            }.also {
                _busy.value = false
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
