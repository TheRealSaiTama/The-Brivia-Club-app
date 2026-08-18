package com.briviaclub.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.briviaclub.app.data.local.AppDatabase
import com.briviaclub.app.data.local.entity.ActivityEntity
import com.briviaclub.app.data.local.entity.MatchEntity
import com.briviaclub.app.data.local.entity.MessageEntity
import com.briviaclub.app.data.local.entity.PaymentEntity
import com.briviaclub.app.data.local.entity.ReportBlockEntity
import com.briviaclub.app.data.local.entity.SubscriptionEntity
import com.briviaclub.app.data.local.entity.UserEntity
import com.briviaclub.app.data.local.entity.UserFilterEntity
import com.briviaclub.app.data.local.repository.BriviaRepository
import com.briviaclub.app.notification.AppNotification
import com.briviaclub.app.notification.BriviaNotificationManager
import com.briviaclub.app.notification.NotificationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CommunityStats(
    val totalUsers: Int = 12450,
    val totalSwipes: Int = 84320,
    val totalMatches: Int = 3890,
    val matchRate: Int = 88,
    val totalRevenue: Double = 18450.0
)

class BriviaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BriviaRepository
    private val notificationManager = BriviaNotificationManager.getInstance(application)

    init {
        val db = AppDatabase.getDatabase(application)
        repository = BriviaRepository(db)
        viewModelScope.launch {
            repository.initializeDatabaseIfEmpty()
            refreshFeed()
        }
    }

    val notifications: StateFlow<List<AppNotification>> = notificationManager.notifications
    val feedAlertsEnabled: StateFlow<Boolean> = notificationManager.feedAlertsEnabled
    val membershipAlertsEnabled: StateFlow<Boolean> = notificationManager.membershipAlertsEnabled

    val unreadNotificationsCount: StateFlow<Int> = notificationManager.notifications
        .map { list -> list.count { !it.isRead } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val currentUser: StateFlow<UserEntity?> = repository.getCurrentUserFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _rawFeed = MutableStateFlow<List<UserEntity>>(emptyList())
    val rawProfiles: StateFlow<List<UserEntity>> = _rawFeed.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _currentFilter = MutableStateFlow(
        UserFilterEntity(
            userId = "me_builder_001",
            locationFilter = "All locations",
            categoryFilter = "All",
            maxDistanceKm = 100,
            minMatchPercent = 60,
            sortBy = "match_percent"
        )
    )
    val currentFilter: StateFlow<UserFilterEntity> = _currentFilter.asStateFlow()

    val filteredFeed: StateFlow<List<UserEntity>> = combine(
        _rawFeed,
        _searchQuery,
        _currentFilter,
        currentUser
    ) { deck, query, filter, me ->
        var list = deck

        // Search query
        if (query.isNotBlank()) {
            val q = query.lowercase().trim()
            list = list.filter {
                it.name.lowercase().contains(q) ||
                    it.role.lowercase().contains(q) ||
                    it.skillsJson.lowercase().contains(q) ||
                    it.location.lowercase().contains(q)
            }
        }

        // Location filter
        if (filter.locationFilter != "All locations" && filter.locationFilter.isNotBlank()) {
            list = list.filter {
                it.location.contains(filter.locationFilter, ignoreCase = true)
            }
        }

        // Category filter
        if (filter.categoryFilter != "All" && filter.categoryFilter.isNotBlank()) {
            list = list.filter {
                it.skillsJson.contains(filter.categoryFilter, ignoreCase = true) ||
                    it.lookingForJson.contains(filter.categoryFilter, ignoreCase = true) ||
                    it.role.contains(filter.categoryFilter, ignoreCase = true)
            }
        }

        // Match sorting & min match
        if (me != null) {
            list = list.sortedByDescending { candidate ->
                when (filter.sortBy) {
                    "experience" -> candidate.experienceYears
                    "recent" -> candidate.createdAt.toInt()
                    else -> repository.calculateMatchScore(me, candidate)
                }
            }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val matches: StateFlow<List<MatchEntity>> = repository.getMatchesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activities: StateFlow<List<ActivityEntity>> = repository.getActivitiesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val subscription: StateFlow<SubscriptionEntity?> = repository.getSubscriptionFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val payments: StateFlow<List<PaymentEntity>> = repository.getPaymentsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reports: StateFlow<List<ReportBlockEntity>> = repository.getAllReportsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeChatMatchId = MutableStateFlow<String?>(null)
    val activeChatMatchId: StateFlow<String?> = _activeChatMatchId.asStateFlow()

    private val _currentChatMessages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val currentChatMessages: StateFlow<List<MessageEntity>> = _currentChatMessages.asStateFlow()

    private val _matchCelebration = MutableStateFlow<UserEntity?>(null)
    val matchCelebration: StateFlow<UserEntity?> = _matchCelebration.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    val stats: StateFlow<CommunityStats> = MutableStateFlow(
        CommunityStats(
            totalUsers = 12450,
            totalSwipes = 84320,
            totalMatches = 3890,
            matchRate = 88,
            totalRevenue = 18450.0
        )
    ).asStateFlow()

    fun refreshFeed() {
        viewModelScope.launch {
            val candidates = repository.getFeedCandidates()
            _rawFeed.value = candidates
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun updateFilters(
        location: String,
        category: String,
        distance: Int,
        minMatch: Int,
        sortBy: String
    ) {
        val updated = UserFilterEntity(
            userId = currentUser.value?.id ?: "me_builder_001",
            locationFilter = location,
            categoryFilter = category,
            maxDistanceKm = distance,
            minMatchPercent = minMatch,
            sortBy = sortBy
        )
        _currentFilter.value = updated
        viewModelScope.launch {
            repository.saveFilter(updated)
        }
    }

    fun handleSwipe(targetUser: UserEntity, action: String) {
        viewModelScope.launch {
            val match = repository.recordSwipe(targetUser.id, action)
            // Remove from raw feed locally
            _rawFeed.value = _rawFeed.value.filter { it.id != targetUser.id }

            if (match != null) {
                _matchCelebration.value = targetUser
            }
        }
    }

    fun dismissMatchCelebration() {
        _matchCelebration.value = null
    }

    fun openChat(matchId: String) {
        _activeChatMatchId.value = matchId
        viewModelScope.launch {
            repository.markMessagesRead(matchId)
            repository.getMessagesFlow(matchId).collect { msgs ->
                _currentChatMessages.value = msgs
            }
        }
    }

    fun sendMessage(matchId: String, receiverId: String, text: String, type: String = "text") {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.sendMessage(matchId, receiverId, text.trim(), type)
        }
    }

    suspend fun getUserById(userId: String): UserEntity? {
        return repository.getUserById(userId)
    }

    fun updateProfile(
        name: String,
        role: String,
        headline: String,
        bio: String,
        location: String,
        age: Int,
        experienceYears: Int,
        photoUrl: String,
        skills: List<String>,
        lookingFor: List<String>,
        isVisible: Boolean
    ) {
        viewModelScope.launch {
            val existing = currentUser.value ?: return@launch
            val updated = existing.copy(
                name = name.trim(),
                role = role.trim(),
                headline = headline.trim(),
                bio = bio.trim(),
                location = location.trim(),
                age = age,
                experienceYears = experienceYears,
                photoUrlsJson = photoUrl,
                skillsJson = skills.joinToString(", "),
                lookingForJson = lookingFor.joinToString(", "),
                isVisible = isVisible,
                onboardingCompleted = true
            )
            repository.updateProfile(updated)
        }
    }

    fun upgradePlan(
        planId: String,
        planName: String,
        amount: Double,
        gateway: String,
        couponCode: String,
        onSuccess: (PaymentEntity) -> Unit
    ) {
        viewModelScope.launch {
            val payment = repository.upgradeSubscription(
                planId = planId,
                planName = planName,
                amount = amount,
                gateway = gateway,
                couponCode = couponCode
            )
            val perks = when (planId) {
                "founder_vip" -> "Gold VIP activated! 5x priority discover boost, 20 Super Likes, and Demo Day access."
                "pro" -> "Silver Pro Builder activated! 2x discover boost, 5 Super Likes, and unlimited matches."
                else -> "Bronze Community activated! Free lifetime networking and member directory access."
            }
            notificationManager.sendMembershipStatusNotification(
                tierName = planName,
                status = "Active",
                perksSummary = perks
            )
            onSuccess(payment)
        }
    }

    fun reportUser(userId: String, reason: String) {
        viewModelScope.launch {
            repository.reportOrBlockUser(userId, "report", reason)
        }
    }

    fun blockUser(userId: String, reason: String) {
        viewModelScope.launch {
            repository.reportOrBlockUser(userId, "block", reason)
            _rawFeed.value = _rawFeed.value.filter { it.id != userId }
        }
    }

    fun deleteAccount(onDone: () -> Unit) {
        viewModelScope.launch {
            repository.deleteCurrentAccount()
            onDone()
        }
    }

    fun toggleActivityLike(activityId: String) {
        viewModelScope.launch {
            repository.toggleActivityLike(activityId)
        }
    }

    fun postMemberUpdate(text: String, tag: String = "🚀 Building") {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.postMemberUpdate(text, tag)
            val author = currentUser.value?.name ?: "You"
            notificationManager.sendFeedActivityNotification(
                title = "New Activity: $author posted an update $tag",
                body = text.take(120) + if (text.length > 120) "..." else "",
                actorName = author,
                tag = tag
            )
        }
    }

    // Notification Controls & Push Triggers
    fun setFeedAlertsEnabled(enabled: Boolean) {
        notificationManager.setFeedAlertsEnabled(enabled)
    }

    fun setMembershipAlertsEnabled(enabled: Boolean) {
        notificationManager.setMembershipAlertsEnabled(enabled)
    }

    fun markNotificationRead(id: String) {
        notificationManager.markAsRead(id)
    }

    fun markAllNotificationsRead() {
        notificationManager.markAllAsRead()
    }

    fun clearAllNotifications() {
        notificationManager.clearAll()
    }

    fun triggerTestFeedNotification() {
        notificationManager.sendFeedActivityNotification(
            title = "🔥 Rohan Verma shared a milestone in Bangalore",
            body = "Raised \$1.2M seed round for B2B AI Agent infra! Check out the announcement in the feed.",
            actorName = "Rohan Verma",
            tag = "🎉 Milestone"
        )
    }

    fun triggerTestMembershipNotification() {
        val currentPlan = subscription.value?.planName ?: "Founder VIP"
        notificationManager.sendMembershipStatusNotification(
            tierName = currentPlan,
            status = "Verified & Active",
            perksSummary = "Your monthly Super Likes & priority algorithm multipliers have been refreshed for this billing cycle."
        )
    }

    fun toggleDarkTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun calculateMatchScore(candidate: UserEntity): Int {
        val me = currentUser.value ?: return 85
        return repository.calculateMatchScore(me, candidate)
    }
}
