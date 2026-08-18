package com.briviaclub.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    indices = [Index(value = ["matchId"])]
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val matchId: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val type: String = "text", // "text", "system", "icebreaker"
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
