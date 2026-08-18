package com.briviaclub.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String = "",
    val phone: String = "",
    val name: String,
    val passwordHash: String = "",
    val role: String = "Builder",
    val headline: String = "",
    val bio: String = "",
    val location: String = "Bengaluru",
    val age: Int = 25,
    val experienceYears: Int = 3,
    val photoUrlsJson: String = "", // Comma-separated or JSON list
    val skillsJson: String = "AI / ML, Fullstack", // Comma-separated
    val lookingForJson: String = "Co-founder, Hackathon", // Comma-separated
    val isVerified: Boolean = true,
    val isPremium: Boolean = false,
    val isVisible: Boolean = true,
    val viewsCount: Int = 12,
    val onboardingCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
