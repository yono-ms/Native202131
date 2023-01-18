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

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE login = :login AND cached_at > :cachedAt LIMIT 1)")
    suspend fun existsCache(login: String, cachedAt: Long): Boolean

    @Query("SELECT NOT EXISTS(SELECT 1 FROM users WHERE login = :login AND updated_at = :updateAt LIMIT 1)")
    suspend fun changed(login: String, updateAt: String): Boolean

    @Query("SELECT CASE WHEN COUNT(*) = 0 THEN 0 ELSE id END FROM users WHERE login = :login LIMIT 1")
    suspend fun getId(login: String): Int

    @Query("SELECT * FROM users ORDER BY cached_at DESC")
    fun loadAllUser(): Flow<List<UserEntity>>
}
