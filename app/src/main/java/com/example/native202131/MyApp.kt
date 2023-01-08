package com.example.native202131

import android.app.Application
import androidx.room.Room
import com.example.native202131.database.AppDatabase
import com.example.native202131.database.RepoDao
import com.example.native202131.database.UserDao
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MyApp : Application() {
    companion object {
        private lateinit var logger: Logger
        fun getLogger(): Logger {
            return logger
        }

        private lateinit var db: AppDatabase
        fun getDb(): AppDatabase {
            return db
        }
    }

    override fun onCreate() {
        super.onCreate()
        logger = LoggerFactory.getLogger(javaClass)
        logger.info("MyApp onCreate")

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration().build()
    }
}

val logger: Logger
    get() = MyApp.getLogger()

val userDao: UserDao
    get() = MyApp.getDb().userDao()

val repoDao: RepoDao
    get() = MyApp.getDb().repoDao()
