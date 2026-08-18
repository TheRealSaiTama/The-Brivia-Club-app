package com.briviaclub.app.data.local.repository

import com.briviaclub.app.data.local.AppDatabase
import com.briviaclub.app.data.local.entity.ActivityEntity
import com.briviaclub.app.data.local.entity.MatchEntity
import com.briviaclub.app.data.local.entity.MessageEntity
import com.briviaclub.app.data.local.entity.PaymentEntity
import com.briviaclub.app.data.local.entity.ReportBlockEntity
import com.briviaclub.app.data.local.entity.SubscriptionEntity
import com.briviaclub.app.data.local.entity.SwipeEntity
import com.briviaclub.app.data.local.entity.UserEntity
import com.briviaclub.app.data.local.entity.UserFilterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.MessageDigest
import java.util.UUID

class BriviaRepository(private val database: AppDatabase) {

    private val userDao = database.userDao()
    private val swipeDao = database.swipeDao()
    private val matchDao = database.matchDao()
    private val messageDao = database.messageDao()
    private val filterDao = database.filterDao()
    private val subscriptionDao = database.subscriptionDao()
    private val paymentDao = database.paymentDao()
    private val reportBlockDao = database.reportBlockDao()
    private val activityDao = database.activityDao()

    private val _currentUserId = MutableStateFlow("me_builder_001")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    suspend fun initializeDatabaseIfEmpty() {
        val existing = userDao.getUserById("builder_1")
        if (existing == null) {
            userDao.insertUsers(SeedData.initialBuilders)
        }

        // Initialize default activities if empty
        if (activityDao.getActivityCount() == 0) {
            activityDao.insertActivities(SeedData.initialActivities)
        }

        // Initialize default current user if not present
        val me = userDao.getUserById(_currentUserId.value)
        if (me == null) {
            val defaultMe = UserEntity(
                id = _currentUserId.value,
                email = "founder@briviaclub.io",
                phone = "+91 99887 76655",
                name = "Aarav Sharma",
                role = "Founding Engineer & AI Hacker",
                headline = "Building Autonomous Agents & Mobile Systems",
                bio = "Full-stack developer focused on Kotlin, React, and local AI models. Looking for a product co-founder and growth partners for our next high-growth startup.",
                location = "Bengaluru",
                age = 26,
                experienceYears = 4,
                photoUrlsJson = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=800&auto=format&fit=crop&q=80",
                skillsJson = "AI / ML, Fullstack, Mobile, SaaS, UI/UX",
                lookingForJson = "Co-founder, Hackathon Partner, Teammates",
                isVerified = true,
                isPremium = false,
                viewsCount = 64,
                onboardingCompleted = true
            )
            userDao.insertUser(defaultMe)
            subscriptionDao.insertSubscription(
                SubscriptionEntity(
                    userId = _currentUserId.value,
                    planId = "free",
                    planName = "Brivia Basic",
                    superLikesRemaining = 3,
                    dailySwipesUsed = 2
                )
            )
            filterDao.saveUserFilter(
                UserFilterEntity(
                    userId = _currentUserId.value,
                    locationFilter = "All locations",
                    categoryFilter = "All",
                    maxDistanceKm = 200,
                    minMatchPercent = 60,
                    sortBy = "match_percent"
                )
            )

            // Seed an initial mutual match with Ananya Rao for instant chat experience
            val initialMatch = MatchEntity(
                id = "match_${_currentUserId.value}_builder_1",
                user1Id = _currentUserId.value,
                user2Id = "builder_1",
                matchScore = 94,
                lastMessage = "Hey Aarav! Loved your AI devtool stack on GitHub. Let's sync!",
                lastMessageTime = System.currentTimeMillis() - 1000 * 60 * 45,
                unreadCount = 1
            )
            matchDao.insertMatch(initialMatch)
            messageDao.insertMessage(
                MessageEntity(
                    matchId = initialMatch.id,
                    senderId = "system",
                    receiverId = _currentUserId.value,
                    text = "🚀 You and Ananya Rao both liked each other! Start building together.",
                    type = "system",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60
                )
            )
            messageDao.insertMessage(
                MessageEntity(
                    matchId = initialMatch.id,
                    senderId = "builder_1",
                    receiverId = _currentUserId.value,
                    text = "Hey Aarav! Loved your AI devtool stack on GitHub. Let's sync!",
                    type = "text",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 45,
                    isRead = false
                )
            )

            // Seed user's initial match activity
            activityDao.insertActivity(
                ActivityEntity(
                    id = "act_me_init",
                    userId = _currentUserId.value,
                    actorName = "You",
                    actorAvatarUrl = defaultMe.photoUrlsJson.split(",").firstOrNull().orEmpty(),
                    actorRole = defaultMe.role,
                    actionType = "MATCH",
                    title = "Connected with Ananya Rao",
                    description = "Mutual match on Product & Generative AI Design.",
                    targetId = "builder_1",
                    targetName = "Ananya Rao",
                    targetAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800",
                    badgeText = "🔥 94% Match",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 45,
                    likesCount = 5,
                    isLikedByMe = true
                )
            )
        }
    }

    // Auth & Security
    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun signUpWithEmailOrPhone(
        name: String,
        email: String,
        phone: String,
        password: String
    ): Result<UserEntity> {
        if (email.isNotBlank() && !email.contains("@")) {
            return Result.failure(IllegalArgumentException("Please enter a valid email address."))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters."))
        }

        val newId = "user_" + UUID.randomUUID().toString().take(8)
        val newUser = UserEntity(
            id = newId,
            email = email.trim(),
            phone = phone.trim(),
            name = name.trim(),
            passwordHash = hashPassword(password),
            role = "Builder",
            headline = "New Brivia Club Member",
            bio = "Excited to connect with builders and ship projects together.",
            location = "Bengaluru",
            isVerified = true,
            onboardingCompleted = false
        )
        userDao.insertUser(newUser)
        _currentUserId.value = newId

        subscriptionDao.insertSubscription(
            SubscriptionEntity(userId = newId, planId = "free", planName = "Brivia Basic")
        )
        filterDao.saveUserFilter(UserFilterEntity(userId = newId))

        return Result.success(newUser)
    }

    suspend fun loginWithCredentials(identifier: String, password: String): Result<UserEntity> {
        val user = if (identifier.contains("@")) {
            userDao.getUserByEmail(identifier.trim())
        } else {
            userDao.getUserByPhone(identifier.trim())
        }

        if (user == null) {
            return Result.failure(IllegalArgumentException("No builder account found with this identifier."))
        }
        val hashed = hashPassword(password)
        if (user.passwordHash.isNotBlank() && user.passwordHash != hashed) {
            return Result.failure(IllegalArgumentException("Invalid password. Please try again."))
        }
        _currentUserId.value = user.id
        return Result.success(user)
    }

    fun getCurrentUserFlow(): Flow<UserEntity?> = userDao.getUserByIdFlow(_currentUserId.value)

    suspend fun getCurrentUser(): UserEntity? = userDao.getUserById(_currentUserId.value)

    suspend fun getUserById(userId: String): UserEntity? = userDao.getUserById(userId)

    suspend fun updateProfile(user: UserEntity) {
        userDao.updateUser(user)
        // Log activity
        val actId = "act_" + UUID.randomUUID().toString().take(8)
        activityDao.insertActivity(
            ActivityEntity(
                id = actId,
                userId = user.id,
                actorName = user.name.ifBlank { "You" },
                actorAvatarUrl = user.photoUrlsJson.split(",").firstOrNull().orEmpty(),
                actorRole = user.role.ifBlank { "Builder" },
                actionType = "PROFILE_UPDATE",
                title = "Updated Profile & Skills",
                description = "Refreshed builder background and skills: ${user.skillsJson.take(60)}",
                badgeText = "⚡ Profile Updated",
                timestamp = System.currentTimeMillis(),
                likesCount = 1,
                isLikedByMe = true
            )
        )
    }

    suspend fun deleteCurrentAccount() {
        val id = _currentUserId.value
        userDao.deleteUserById(id)
        swipeDao.clearUserSwipes(id)
        _currentUserId.value = "me_builder_001"
    }

    // Discover & Swipes
    suspend fun getFeedCandidates(): List<UserEntity> {
        val myId = _currentUserId.value
        val swipedIds = swipeDao.getSwipedUserIds(myId)
        val blockedIds = reportBlockDao.getBlockedUserIds(myId)
        val allOther = userDao.getAllOtherUsers(myId)

        return allOther.filter { candidate ->
            !swipedIds.contains(candidate.id) && !blockedIds.contains(candidate.id)
        }
    }

    suspend fun recordSwipe(targetUserId: String, action: String): MatchEntity? {
        val myId = _currentUserId.value
        swipeDao.insertSwipe(
            SwipeEntity(
                userId = myId,
                targetUserId = targetUserId,
                action = action
            )
        )
        subscriptionDao.incrementDailySwipes(myId)

        val targetUser = userDao.getUserById(targetUserId)
        val me = userDao.getUserById(myId)

        if (action == "like" || action == "superlike") {
            // Check mutual like or simulate high probability match for engaging UX
            if (targetUser == null || me == null) return null
            val matchScore = calculateMatchScore(me, targetUser)

            val matchId = "match_${myId}_${targetUserId}"
            val newMatch = MatchEntity(
                id = matchId,
                user1Id = myId,
                user2Id = targetUserId,
                matchScore = matchScore,
                lastMessage = if (action == "superlike") "⭐ Super liked your profile! Say hi!" else "You matched! Start building together.",
                lastMessageTime = System.currentTimeMillis(),
                unreadCount = 0
            )
            matchDao.insertMatch(newMatch)

            // Insert system greeting message
            messageDao.insertMessage(
                MessageEntity(
                    matchId = matchId,
                    senderId = "system",
                    receiverId = targetUserId,
                    text = "🚀 Match created! You and ${targetUser.name} are ready to build together.",
                    type = "system"
                )
            )

            // Log activity item
            val actId = "act_" + UUID.randomUUID().toString().take(8)
            activityDao.insertActivity(
                ActivityEntity(
                    id = actId,
                    userId = myId,
                    actorName = me.name.ifBlank { "You" },
                    actorAvatarUrl = me.photoUrlsJson.split(",").firstOrNull().orEmpty(),
                    actorRole = me.role,
                    actionType = if (action == "superlike") "SUPERLIKE" else "MATCH",
                    title = if (action == "superlike") "Super-Liked ${targetUser.name}" else "New Match with ${targetUser.name}",
                    description = if (action == "superlike") "Sent a priority introduction to ${targetUser.name} (${targetUser.role})" else "Connected with ${targetUser.name} on common builder goals ($matchScore% Match)",
                    targetId = targetUser.id,
                    targetName = targetUser.name,
                    targetAvatarUrl = targetUser.photoUrlsJson.split(",").firstOrNull().orEmpty(),
                    badgeText = if (action == "superlike") "⭐ Super Like" else "🔥 $matchScore% Match",
                    timestamp = System.currentTimeMillis(),
                    likesCount = 3,
                    isLikedByMe = true
                )
            )

            return newMatch
        }
        return null
    }

    fun calculateMatchScore(u1: UserEntity, u2: UserEntity): Int {
        var score = 65
        val u1Skills = u1.skillsJson.split(",").map { it.trim().lowercase() }
        val u2Skills = u2.skillsJson.split(",").map { it.trim().lowercase() }
        val sharedSkills = u1Skills.intersect(u2Skills.toSet()).size
        score += (sharedSkills * 8).coerceAtMost(24)

        if (u1.location.equals(u2.location, ignoreCase = true)) {
            score += 8
        }
        if (u1.isVerified && u2.isVerified) {
            score += 5
        }
        return score.coerceIn(72, 99)
    }

    // Match & Messages
    fun getMatchesFlow(): Flow<List<MatchEntity>> = matchDao.getMatchesForUserFlow(_currentUserId.value)

    fun getMessagesFlow(matchId: String): Flow<List<MessageEntity>> = messageDao.getMessagesForMatchFlow(matchId)

    suspend fun sendMessage(matchId: String, receiverId: String, text: String, type: String = "text") {
        val myId = _currentUserId.value
        messageDao.insertMessage(
            MessageEntity(
                matchId = matchId,
                senderId = myId,
                receiverId = receiverId,
                text = text,
                type = type,
                timestamp = System.currentTimeMillis()
            )
        )
        matchDao.updateLastMessage(matchId, text, System.currentTimeMillis())
    }

    suspend fun markMessagesRead(matchId: String) {
        messageDao.markMessagesAsRead(matchId, _currentUserId.value)
    }

    // Filters
    fun getFilterFlow(): Flow<UserFilterEntity?> = filterDao.getUserFilterFlow(_currentUserId.value)

    suspend fun saveFilter(filter: UserFilterEntity) {
        filterDao.saveUserFilter(filter)
    }

    // Subscriptions & Payments
    fun getSubscriptionFlow(): Flow<SubscriptionEntity?> = subscriptionDao.getSubscriptionFlow(_currentUserId.value)

    fun getPaymentsFlow(): Flow<List<PaymentEntity>> = paymentDao.getPaymentsForUserFlow(_currentUserId.value)

    suspend fun upgradeSubscription(
        planId: String,
        planName: String,
        amount: Double,
        gateway: String,
        couponCode: String = ""
    ): PaymentEntity {
        val myId = _currentUserId.value
        val inv = "INV-${System.currentTimeMillis().toString().takeLast(6)}"
        val txId = "TXN_${UUID.randomUUID().toString().take(10).uppercase()}"

        val payment = PaymentEntity(
            transactionId = txId,
            userId = myId,
            amount = amount,
            planName = planName,
            gateway = gateway,
            status = "success",
            invoiceNumber = inv,
            couponCode = couponCode
        )
        paymentDao.insertPayment(payment)

        val updatedSub = SubscriptionEntity(
            userId = myId,
            planId = planId,
            planName = planName,
            status = "active",
            price = amount,
            expiresAt = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000),
            superLikesRemaining = if (planId == "founder_vip") 20 else 5,
            boostActive = true
        )
        subscriptionDao.insertSubscription(updatedSub)

        // Mark user as premium in UserEntity
        val me = userDao.getUserById(myId)
        if (me != null) {
            userDao.updateUser(me.copy(isPremium = true))
        }

        // Log tier upgrade activity
        val actId = "act_" + UUID.randomUUID().toString().take(8)
        activityDao.insertActivity(
            ActivityEntity(
                id = actId,
                userId = myId,
                actorName = me?.name ?: "You",
                actorAvatarUrl = me?.photoUrlsJson?.split(",")?.firstOrNull().orEmpty(),
                actorRole = me?.role ?: "Builder",
                actionType = "TIER_UPGRADE",
                title = "Upgraded to $planName",
                description = "Unlocked unlimited daily connections and verified priority visibility.",
                badgeText = "👑 $planName",
                timestamp = System.currentTimeMillis(),
                likesCount = 8,
                isLikedByMe = true
            )
        )

        return payment
    }

    // Activity Feed
    fun getActivitiesFlow(): Flow<List<ActivityEntity>> = activityDao.getAllActivitiesFlow()

    suspend fun toggleActivityLike(activityId: String) {
        val activity = activityDao.getActivityById(activityId) ?: return
        val newIsLiked = !activity.isLikedByMe
        val newCount = if (newIsLiked) activity.likesCount + 1 else (activity.likesCount - 1).coerceAtLeast(0)
        activityDao.updateActivityLike(activityId, newCount, newIsLiked)
    }

    suspend fun postMemberUpdate(text: String, tag: String = "🚀 Building") {
        val myId = _currentUserId.value
        val me = userDao.getUserById(myId)
        val actId = "act_" + UUID.randomUUID().toString().take(8)
        activityDao.insertActivity(
            ActivityEntity(
                id = actId,
                userId = myId,
                actorName = me?.name?.ifBlank { "You" } ?: "You",
                actorAvatarUrl = me?.photoUrlsJson?.split(",")?.firstOrNull().orEmpty(),
                actorRole = me?.role ?: "Builder",
                actionType = "COLLAB_POST",
                title = tag,
                description = text.trim(),
                badgeText = tag,
                timestamp = System.currentTimeMillis(),
                likesCount = 1,
                isLikedByMe = true
            )
        )
    }

    // Moderation & Analytics
    suspend fun reportOrBlockUser(targetUserId: String, type: String, reason: String) {
        val myId = _currentUserId.value
        reportBlockDao.insertReportOrBlock(
            ReportBlockEntity(
                reporterId = myId,
                targetUserId = targetUserId,
                type = type,
                reason = reason
            )
        )
    }

    fun getTotalUsersCountFlow(): Flow<Int> = userDao.getTotalUsersCountFlow()
    fun getTotalSwipesCountFlow(): Flow<Int> = swipeDao.getTotalSwipesCountFlow()
    fun getTotalMatchesCountFlow(): Flow<Int> = matchDao.getTotalMatchesCountFlow()
    fun getTotalRevenueFlow(): Flow<Double?> = paymentDao.getTotalRevenueFlow()
    fun getAllReportsFlow(): Flow<List<ReportBlockEntity>> = reportBlockDao.getAllReportsAndBlocksFlow()
}
