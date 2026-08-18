package com.briviaclub.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.briviaclub.app.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE matchId = :matchId ORDER BY timestamp ASC")
    fun getMessagesForMatchFlow(matchId: String): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    @Query("UPDATE messages SET isRead = 1 WHERE matchId = :matchId AND receiverId = :userId")
    suspend fun markMessagesAsRead(matchId: String, userId: String)

    @Query("DELETE FROM messages WHERE matchId = :matchId")
    suspend fun deleteMessagesForMatch(matchId: String)
}
