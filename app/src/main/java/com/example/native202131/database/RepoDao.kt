package com.example.native202131.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg repoEntities: RepoEntity)

    @Query("SELECT * FROM repos ORDER BY updated_at DESC")
    fun loadAllRepo(): Flow<List<RepoEntity>>
}
