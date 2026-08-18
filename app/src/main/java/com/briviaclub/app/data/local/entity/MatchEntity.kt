package com.briviaclub.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "matches",
    indices = [Index(value = ["user1Id", "user2Id"], unique = true)]
)
data class MatchEntity(
    @PrimaryKey val id: String,
    val user1Id: String,
    val user2Id: String,
    val matchedAt: Long = System.currentTimeMillis(),
    val matchScore: Int = 90,
    val lastMessage: String = "You matched! Start building together.",
    val lastMessageTime: Long = System.currentTimeMillis(),
    val unreadCount: Int = 0
)
