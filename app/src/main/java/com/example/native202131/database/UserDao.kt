package com.example.native202131.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

    @Query("SELECT * FROM users WHERE login like :login")
    suspend fun getUser(login: String): UserEntity

    @Query("SELECT * FROM users ORDER BY cached_at DESC")
    suspend fun getAllUser(): List<UserEntity>

    @Query("SELECT * FROM users ORDER BY cached_at DESC")
    fun loadAllUser(): Flow<List<UserEntity>>
}
