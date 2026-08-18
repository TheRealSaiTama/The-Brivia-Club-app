package com.briviaclub.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.briviaclub.app.data.local.entity.MatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches WHERE user1Id = :userId OR user2Id = :userId ORDER BY lastMessageTime DESC")
    fun getMatchesForUserFlow(userId: String): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE id = :matchId LIMIT 1")
    suspend fun getMatchById(matchId: String): MatchEntity?

    @Query("SELECT * FROM matches WHERE (user1Id = :u1 AND user2Id = :u2) OR (user1Id = :u2 AND user2Id = :u1) LIMIT 1")
    suspend fun findMatchBetween(u1: String, u2: String): MatchEntity?

    @Query("SELECT COUNT(*) FROM matches")
    fun getTotalMatchesCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchEntity)

    @Update
    suspend fun updateMatch(match: MatchEntity)

    @Query("UPDATE matches SET lastMessage = :message, lastMessageTime = :time WHERE id = :matchId")
    suspend fun updateLastMessage(matchId: String, message: String, time: Long)

    @Query("DELETE FROM matches WHERE id = :matchId")
    suspend fun deleteMatchById(matchId: String)
}
