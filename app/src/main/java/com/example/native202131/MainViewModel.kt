package com.example.native202131

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.slf4j.LoggerFactory
import java.util.*

class MainViewModel : ViewModel() {
    private val logger by lazy { LoggerFactory.getLogger(javaClass) }

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    fun setMessage(text: String) {
        logger.info("setMessage $text")
        _message.value = text + Date().toString()
    }
}
