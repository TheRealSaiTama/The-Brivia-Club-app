package com.briviaclub.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "swipes",
    indices = [Index(value = ["userId", "targetUserId"], unique = false)]
)
data class SwipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val targetUserId: String,
    val action: String, // "like", "pass", "superlike"
    val timestamp: Long = System.currentTimeMillis()
)
