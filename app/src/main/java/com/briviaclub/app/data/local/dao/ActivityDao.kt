package com.briviaclub.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.briviaclub.app.data.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Query("SELECT * FROM member_activities ORDER BY timestamp DESC")
    fun getAllActivitiesFlow(): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM member_activities WHERE userId = :userId ORDER BY timestamp DESC")
    fun getActivitiesByUserFlow(userId: String): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM member_activities WHERE id = :id LIMIT 1")
    suspend fun getActivityById(id: String): ActivityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(activities: List<ActivityEntity>)

    @Query("UPDATE member_activities SET likesCount = :likesCount, isLikedByMe = :isLikedByMe WHERE id = :id")
    suspend fun updateActivityLike(id: String, likesCount: Int, isLikedByMe: Boolean)

    @Query("DELETE FROM member_activities WHERE id = :id")
    suspend fun deleteActivityById(id: String)

    @Query("SELECT COUNT(*) FROM member_activities")
    suspend fun getActivityCount(): Int
}
