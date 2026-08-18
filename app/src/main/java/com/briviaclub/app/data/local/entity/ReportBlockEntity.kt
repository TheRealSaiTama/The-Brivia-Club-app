package com.briviaclub.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports_blocks")
data class ReportBlockEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reporterId: String,
    val targetUserId: String,
    val type: String, // "report", "block"
    val reason: String,
    val timestamp: Long = System.currentTimeMillis()
)
