package com.example.native202131

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import java.util.*

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
//    val users: StateFlow<List<UserEntity>> = _users

    private val _login = MutableStateFlow("apple")
    val login: StateFlow<String> = _login

    fun updateLogin(text: String) {
        logger.info("updateLogin")
        _login.value = text
    }

    fun onGet() {
        logger.info("onGet")
        viewModelScope.launch {
            runCatching {
                _busy.value = true
                _message.value = ""
                val currentUser = _users.value.firstOrNull { it.login == _login.value }
                logger.debug("currentUser $currentUser")
                if (isUserExpired(currentUser)) {
                    logger.trace("cache expired.")

                    val url = "https://api.github.com/users/${_login.value}"
                    val userJson = Fuel.get(url).awaitString()
                    logger.trace("Fuel users DONE.")

                    val userModel = json.decodeFromString<UserModel>(userJson)
                    logger.trace("Json decode users DONE.")

                    val isUserChanged = userModel.updatedAt != currentUser?.updatedAt
                    logger.debug("isUserChanged $isUserChanged")

                    insertUser(userModel)

                    if (isUserChanged) {
                        logger.trace("user data changed.")


                        val repoJson = Fuel.get(userModel.reposUrl).awaitString()
                        logger.trace("Fuel repos DONE.")

                        val repoModels = json.decodeFromString<List<RepoModel>>(repoJson)
                        logger.trace("Json decode repos DONE.")

                        insertRepos(repoModels, userModel.id)
                        logger.trace("Room insert repos DONE.")
                    }
                }
            }.onSuccess {
                logger.trace("Room getAllRepo DONE.")
            }.onFailure {
                logger.error("onClick", it)
                _message.value = it.localizedMessage ?: ""
            }.also {
                _busy.value = false
            }
        }
    }

    private fun isUserExpired(user: UserEntity?): Boolean {
        logger.info("isUserExpired START $user")
        val cachedTime = user?.cachedAt ?: 0
        val expiredTime = cachedTime + CACHE_TIME_LIMIT
        val result = expiredTime < Date().time
        logger.debug("isUserExpired $result")
        return result
    }

    private suspend fun insertUser(userModel: UserModel) {
        logger.info("insertUser START")
        val list = _users.value.filter { it.login == userModel.login }
        val id = list.firstOrNull()?.id ?: 0
        logger.debug("id $id")
        if (list.any()) {
            logger.info("login ${userModel.login} exist.")
            list.filter { it.id != id }.forEach {
                logger.warn("delete user ${it.login}")
                userDao.delete(it)
            }
        }
        val userEntity = userModel.toEntity(id)
        userDao.insert(userEntity)
        logger.trace("Room insert users DONE.")
        logger.info("insertUser END")
    }

    private suspend fun insertRepos(repoModels: List<RepoModel>, ownerId: Int) {
        logger.info("insertRepos START")
        val aaa = repoDao.getAllRepo(ownerId)
        logger.debug("delete size ${aaa.size}")
        repoDao.deleteAll(*aaa.toTypedArray())
        logger.trace("deleteAll DONE.")
        val list = repoModels.toEntity(ownerId)
        repoDao.insertAll(*list.toTypedArray())
        logger.info("insertRepos END")
    }

    init {
        logger.info("init START")
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
        logger.info("init END")
    }

    companion object {
        const val CACHE_TIME_LIMIT = 1_000
    }
}
