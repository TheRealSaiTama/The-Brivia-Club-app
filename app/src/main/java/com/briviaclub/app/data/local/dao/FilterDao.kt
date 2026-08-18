package com.briviaclub.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.briviaclub.app.data.local.entity.UserFilterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterDao {
    @Query("SELECT * FROM user_filters WHERE userId = :userId LIMIT 1")
    fun getUserFilterFlow(userId: String): Flow<UserFilterEntity?>

    @Query("SELECT * FROM user_filters WHERE userId = :userId LIMIT 1")
    suspend fun getUserFilter(userId: String): UserFilterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserFilter(filter: UserFilterEntity)
}
