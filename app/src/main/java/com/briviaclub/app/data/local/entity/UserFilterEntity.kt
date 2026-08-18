package com.briviaclub.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_filters")
data class UserFilterEntity(
    @PrimaryKey val userId: String,
    val locationFilter: String = "All locations",
    val categoryFilter: String = "All",
    val maxDistanceKm: Int = 100,
    val minMatchPercent: Int = 70,
    val sortBy: String = "match_percent" // "match_percent", "recent", "experience"
)
