package com.briviaclub.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.briviaclub.app.data.local.entity.SwipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SwipeDao {
    @Query("SELECT * FROM swipes WHERE userId = :userId")
    fun getSwipesByUser(userId: String): Flow<List<SwipeEntity>>

    @Query("SELECT * FROM swipes WHERE userId = :userId AND targetUserId = :targetUserId LIMIT 1")
    suspend fun getSwipe(userId: String, targetUserId: String): SwipeEntity?

    @Query("SELECT targetUserId FROM swipes WHERE userId = :userId")
    suspend fun getSwipedUserIds(userId: String): List<String>

    @Query("SELECT COUNT(*) FROM swipes")
    fun getTotalSwipesCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSwipe(swipe: SwipeEntity)

    @Query("DELETE FROM swipes WHERE userId = :userId")
    suspend fun clearUserSwipes(userId: String)
}
