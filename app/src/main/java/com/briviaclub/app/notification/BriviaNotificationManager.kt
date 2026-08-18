package com.briviaclub.app.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.briviaclub.app.MainActivity
import com.briviaclub.app.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

enum class NotificationType {
    FEED_ACTIVITY,
    MEMBERSHIP_UPDATE,
    MATCH_EVENT
}

data class AppNotification(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val body: String,
    val type: NotificationType,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val deepLink: String? = null,
    val badgeEmoji: String = "🔔"
)

class BriviaNotificationManager(private val context: Context) {

    companion object {
        const val CHANNEL_FEED_ACTIVITY = "brivia_feed_activity_channel"
        const val CHANNEL_MEMBERSHIP = "brivia_membership_status_channel"

        const val EXTRA_DESTINATION = "extra_destination"
        const val DESTINATION_FEED = "feed"
        const val DESTINATION_MEMBERSHIP = "membership"
        const val DESTINATION_MATCHES = "matches"

        @Volatile
        private var instance: BriviaNotificationManager? = null

        fun getInstance(context: Context): BriviaNotificationManager {
            return instance ?: synchronized(this) {
                instance ?: BriviaNotificationManager(context.applicationContext).also { instance = it }
            }
        }
    }

    private val prefs = context.getSharedPreferences("brivia_notifications_prefs", Context.MODE_PRIVATE)

    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private val _feedAlertsEnabled = MutableStateFlow(prefs.getBoolean("feed_alerts", true))
    val feedAlertsEnabled: StateFlow<Boolean> = _feedAlertsEnabled.asStateFlow()

    private val _membershipAlertsEnabled = MutableStateFlow(prefs.getBoolean("membership_alerts", true))
    val membershipAlertsEnabled: StateFlow<Boolean> = _membershipAlertsEnabled.asStateFlow()

    init {
        createNotificationChannels()
        seedInitialNotifications()
    }

    private fun seedInitialNotifications() {
        _notifications.value = listOf(
            AppNotification(
                id = "init_notif_1",
                title = "Welcome to The Brivia Club! 🚀",
                body = "Your membership profile is verified. Connect with top Indian tech founders & builders.",
                type = NotificationType.MEMBERSHIP_UPDATE,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 2,
                isRead = true,
                badgeEmoji = "👑",
                deepLink = DESTINATION_MEMBERSHIP
            ),
            AppNotification(
                id = "init_notif_2",
                title = "Ananya Rao posted an update 💡",
                body = "Shipped new AI Figma-to-Code plugin with 1.2k GitHub stars! Check it out in the activity feed.",
                type = NotificationType.FEED_ACTIVITY,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 30,
                isRead = false,
                badgeEmoji = "🔥",
                deepLink = DESTINATION_FEED
            )
        )
    }

    fun setFeedAlertsEnabled(enabled: Boolean) {
        _feedAlertsEnabled.value = enabled
        prefs.edit().putBoolean("feed_alerts", enabled).apply()
    }

    fun setMembershipAlertsEnabled(enabled: Boolean) {
        _membershipAlertsEnabled.value = enabled
        prefs.edit().putBoolean("membership_alerts", enabled).apply()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Channel 1: Feed & Activity Updates
            val feedChannel = NotificationChannel(
                CHANNEL_FEED_ACTIVITY,
                "Club Activity & Feed",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Real-time updates when members post projects, get featured, or react in the feed."
                enableLights(true)
                lightColor = Color.parseColor("#E0A96D")
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 200, 100, 200)
            }

            // Channel 2: Membership & Tier Updates
            val membershipChannel = NotificationChannel(
                CHANNEL_MEMBERSHIP,
                "Membership & VIP Status",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Critical alerts for tier upgrades, plan renewals, Gold/Silver perks, and invoices."
                enableLights(true)
                lightColor = Color.parseColor("#D4AF37")
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 250, 150, 250)
            }

            notificationManager.createNotificationChannel(feedChannel)
            notificationManager.createNotificationChannel(membershipChannel)
        }
    }

    /**
     * Send Push Notification for New Feed Activity
     */
    fun sendFeedActivityNotification(
        title: String,
        body: String,
        actorName: String? = null,
        tag: String = "Feed Update"
    ) {
        if (!_feedAlertsEnabled.value) return

        val appNotification = AppNotification(
            title = title,
            body = body,
            type = NotificationType.FEED_ACTIVITY,
            badgeEmoji = "⚡",
            deepLink = DESTINATION_FEED
        )
        addNotification(appNotification)

        postSystemNotification(
            channelId = CHANNEL_FEED_ACTIVITY,
            notificationId = (System.currentTimeMillis() % 100000).toInt() + 1000,
            title = title,
            body = body,
            destination = DESTINATION_FEED,
            subText = tag
        )
    }

    /**
     * Send Push Notification for Membership Status Changes
     */
    fun sendMembershipStatusNotification(
        tierName: String,
        status: String,
        perksSummary: String
    ) {
        if (!_membershipAlertsEnabled.value) return

        val title = "Tier Update: $tierName ($status)"
        val body = perksSummary

        val appNotification = AppNotification(
            title = title,
            body = body,
            type = NotificationType.MEMBERSHIP_UPDATE,
            badgeEmoji = when {
                tierName.contains("Gold", true) || tierName.contains("VIP", true) -> "🥇"
                tierName.contains("Silver", true) || tierName.contains("Pro", true) -> "🥈"
                else -> "🥉"
            },
            deepLink = DESTINATION_MEMBERSHIP
        )
        addNotification(appNotification)

        postSystemNotification(
            channelId = CHANNEL_MEMBERSHIP,
            notificationId = (System.currentTimeMillis() % 100000).toInt() + 2000,
            title = title,
            body = body,
            destination = DESTINATION_MEMBERSHIP,
            subText = "Brivia Membership"
        )
    }

    private fun addNotification(notification: AppNotification) {
        val current = _notifications.value.toMutableList()
        current.add(0, notification)
        _notifications.value = current.take(30)
    }

    fun markAsRead(id: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }

    fun markAllAsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    fun clearAll() {
        _notifications.value = emptyList()
    }

    private fun postSystemNotification(
        channelId: String,
        notificationId: Int,
        title: String,
        body: String,
        destination: String,
        subText: String? = null
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // Cannot post system notification without permission; in-app tray is still updated
                return
            }
        }

        try {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(EXTRA_DESTINATION, destination)
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setColor(Color.parseColor("#E0A96D"))

            if (!subText.isNullOrBlank()) {
                builder.setSubText(subText)
            }

            val manager = NotificationManagerCompat.from(context)
            manager.notify(notificationId, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
