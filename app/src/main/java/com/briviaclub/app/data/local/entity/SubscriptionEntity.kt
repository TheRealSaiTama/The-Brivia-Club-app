package com.briviaclub.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey val userId: String,
    val planId: String = "free", // "free", "pro", "founder_vip"
    val planName: String = "Brivia Free",
    val status: String = "active", // "active", "cancelled", "expired"
    val price: Double = 0.0,
    val startDate: Long = System.currentTimeMillis(),
    val expiresAt: Long = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000),
    val superLikesRemaining: Int = 1,
    val boostActive: Boolean = false,
    val dailySwipesUsed: Int = 0,
    val lastSwipeDate: Long = System.currentTimeMillis()
)
