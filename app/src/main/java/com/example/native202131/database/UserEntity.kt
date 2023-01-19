package com.example.native202131.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users", indices = [Index(value = ["login"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "cached_at")
    val cachedAt: Long,
    @ColumnInfo(name = "login")
    val login: String,
    @ColumnInfo(name = "login_id")
    val loginId: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "repos_url")
    val reposUrl: String,
    @ColumnInfo(name = "public_repos")
    val publicRepos: Int,
    @ColumnInfo(name = "updated_at")
    val updatedAt: String,
)
