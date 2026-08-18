package com.briviaclub.app.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.briviaclub.app.ui.theme.AmberGold
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.DeepWine

enum class BottomTab(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    DISCOVER("discover", "Discover", Icons.Filled.Style, Icons.Outlined.Style),
    MATCHES("matches", "Chat", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline),
    MEMBERSHIP("membership", "VIP Club", Icons.Filled.WorkspacePremium, Icons.Outlined.WorkspacePremium),
    PROFILE("profile", "Profile", Icons.Filled.Person, Icons.Outlined.PersonOutline),
    ANALYTICS("analytics", "Metrics", Icons.Filled.Analytics, Icons.Outlined.Analytics)
}

/**
 * Ultra-sleek, luxury floating navigation island with animated scale & haptic transitions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BriviaBottomNavBar(
    currentRoute: String,
    unreadMatchesCount: Int = 0,
    onTabSelected: (BottomTab) -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.red < 0.2f
    val haptic = LocalHapticFeedback.current

    // Ambient floating container
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (isDark) 24.dp else 12.dp,
                    shape = RoundedCornerShape(26.dp),
                    spotColor = if (isDark) DeepWine.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.15f)
                ),
            shape = RoundedCornerShape(26.dp),
            color = if (isDark) Color(0xFF14070A) else Color.White,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isDark) ChampagneGold.copy(alpha = 0.25f) else Color(0xFFEFE6E8)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomTab.values().forEach { tab ->
                    val isSelected = currentRoute == tab.route
                    LuxuryNavBarItem(
                        tab = tab,
                        isSelected = isSelected,
                        unreadCount = if (tab == BottomTab.MATCHES) unreadMatchesCount else 0,
                        isDark = isDark,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onTabSelected(tab)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LuxuryNavBarItem(
    tab: BottomTab,
    isSelected: Boolean,
    unreadCount: Int,
    isDark: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Animated scale and color states
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.08f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 400f),
        label = "tab_scale"
    )

    val activeColor = if (isDark) ChampagneGold else DeepWine
    val inactiveColor = if (isDark) Color(0xFF8C7B83) else Color(0xFF8A7F85)

    val iconTint by animateColorAsState(
        targetValue = if (isSelected) activeColor else inactiveColor,
        animationSpec = tween(220),
        label = "icon_tint"
    )

    val pillBgColor by animateColorAsState(
        targetValue = if (isSelected) {
            if (isDark) DeepWine.copy(alpha = 0.45f) else Color(0xFFF7ECEF)
        } else {
            Color.Transparent
        },
        animationSpec = tween(220),
        label = "pill_bg"
    )

    // Unread notification badge pulse
    val infiniteTransition = rememberInfiniteTransition(label = "badge_pulse")
    val badgeScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "badge_scale"
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(pillBgColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .scale(scale),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon with optional BadgedBox
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (unreadCount > 0) {
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = DeepWine,
                            contentColor = Color.White,
                            modifier = Modifier
                                .scale(badgeScale)
                                .size(16.dp)
                        ) {
                            Text(
                                text = unreadCount.toString(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = tab.title,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }
            } else {
                Icon(
                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                    contentDescription = tab.title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        // Tab Label
        Text(
            text = tab.title,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontFamily = if (isSelected) FontFamily.Serif else FontFamily.Default,
            letterSpacing = if (isSelected) 0.5.sp else 0.sp,
            color = iconTint
        )

        // Delicate active glowing dot underneath
        if (isSelected) {
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .size(3.5.dp)
                    .clip(CircleShape)
                    .background(if (isDark) ChampagneGold else DeepWine)
            )
        } else {
            Spacer(modifier = Modifier.height(5.5.dp))
        }
    }
}
