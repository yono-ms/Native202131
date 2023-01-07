package com.example.native202131

import android.app.Application
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MyApp : Application() {
    companion object {
        private lateinit var logger: Logger
        fun getLogger(): Logger {
            return logger
        }
    }

    override fun onCreate() {
        super.onCreate()
        logger = LoggerFactory.getLogger(javaClass)
        logger.info("MyApp onCreate")
    }
}

val logger: Logger
    get() {
        return MyApp.getLogger()
    }
