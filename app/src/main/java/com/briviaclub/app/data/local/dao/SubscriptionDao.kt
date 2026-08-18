package com.briviaclub.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.briviaclub.app.data.local.entity.SubscriptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {
    @Query("SELECT * FROM subscriptions WHERE userId = :userId LIMIT 1")
    fun getSubscriptionFlow(userId: String): Flow<SubscriptionEntity?>

    @Query("SELECT * FROM subscriptions WHERE userId = :userId LIMIT 1")
    suspend fun getSubscription(userId: String): SubscriptionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscription(subscription: SubscriptionEntity)

    @Update
    suspend fun updateSubscription(subscription: SubscriptionEntity)

    @Query("UPDATE subscriptions SET dailySwipesUsed = dailySwipesUsed + 1 WHERE userId = :userId")
    suspend fun incrementDailySwipes(userId: String)

    @Query("UPDATE subscriptions SET dailySwipesUsed = 0, lastSwipeDate = :today WHERE userId = :userId")
    suspend fun resetDailySwipes(userId: String, today: Long)
}
