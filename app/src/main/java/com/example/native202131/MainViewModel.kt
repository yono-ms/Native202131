package com.example.native202131

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.native202131.network.RepoModel
import com.example.native202131.network.UserModel
import com.example.native202131.network.toEntity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.github.kittinunf.fuel.coroutines.awaitStringResponse
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

    fun onGet(login: String) {
        logger.info("onGet")
        viewModelScope.launch {
            runCatching {
                _busy.value = true
                _message.value = ""
                val isExist = userDao.existsCache(login, Date().time - CACHE_TIME_LIMIT)
                if (isExist) {
                    logger.info("cache exist.")
                } else {
                    logger.info("cache expired.")
                    val url = "https://api.github.com/users/${login}"
                    val userJson = Fuel.get(url).awaitString()
                    val userModel = json.decodeFromString<UserModel>(userJson)
                    val isUserChanged = userDao.changed(login, userModel.updatedAt)
                    logger.debug("isUserChanged $isUserChanged")
                    insertUser(userModel)
                    if (isUserChanged) {
                        logger.info("user data changed.")
                        val repoEntities = repoDao.getAllRepo(userModel.id)
                        logger.debug("old delete size ${repoEntities.size}")
                        repoDao.deleteAll(*repoEntities.toTypedArray())
                        logger.trace("deleteAll DONE.")
                        val loopCount =
                            userModel.publicRepos / 100 + if (userModel.publicRepos % 100 > 0) 1 else 0
                        for (page in 1..loopCount) {
                            val parameters = listOf("page" to page.toString(), "per_page" to "100")
                            val (request, _, repoJson) = Fuel.get(userModel.reposUrl, parameters)
                                .awaitStringResponse()
                            logger.debug(request.toString())
                            val repoModels = json.decodeFromString<List<RepoModel>>(repoJson)
                            logger.debug("repoModels size ${repoModels.size}")
                            insertRepos(repoModels, userModel.id)
                        }
                    } else {
                        logger.info("user data not changed.")
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

    private suspend fun insertUser(userModel: UserModel) {
        logger.info("insertUser START")
        val id = userDao.getId(userModel.login)
        logger.debug("id $id")
        val userEntity = userModel.toEntity(id)
        userDao.insert(userEntity)
        logger.trace("Room insert users DONE.")
        logger.info("insertUser END")
    }

    private suspend fun insertRepos(repoModels: List<RepoModel>, ownerId: Int) {
        logger.info("insertRepos START")
        val list = repoModels.toEntity(ownerId)
        repoDao.insertAll(*list.toTypedArray())
        logger.info("insertRepos END")
    }

    companion object {
        const val CACHE_TIME_LIMIT = 10_000
    }
}
