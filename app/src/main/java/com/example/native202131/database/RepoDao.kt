package com.example.native202131.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg repoEntities: RepoEntity)

    @Delete
    suspend fun deleteAll(vararg repoEntity: RepoEntity)

    @Query("SELECT * FROM repos WHERE owner_id = :ownerId ORDER BY updated_at DESC")
    suspend fun getAllRepo(ownerId: Int): List<RepoEntity>

    @Query("SELECT * FROM repos WHERE owner_id = (SELECT login_id FROM users WHERE login = :login) ORDER BY updated_at DESC")
    fun loadSelectedRepo(login: String): Flow<List<RepoEntity>>
}
