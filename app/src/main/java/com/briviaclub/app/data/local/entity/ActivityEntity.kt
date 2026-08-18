package com.briviaclub.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "member_activities")
data class ActivityEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val actorName: String,
    val actorAvatarUrl: String,
    val actorRole: String,
    val actionType: String, // MATCH, SUPERLIKE, PROFILE_UPDATE, TIER_UPGRADE, COLLAB_POST, SKILL_UPDATE, CHEER
    val title: String,
    val description: String,
    val targetId: String? = null,
    val targetName: String? = null,
    val targetAvatarUrl: String? = null,
    val badgeText: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val likesCount: Int = 0,
    val isLikedByMe: Boolean = false
)
