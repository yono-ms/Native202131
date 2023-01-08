package com.example.native202131.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repos")
data class RepoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "owner_id")
    val ownerId: Int,
    @ColumnInfo(name = "repo_id")
    val repoId: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "updated_at")
    val updatedAt: String,
)
