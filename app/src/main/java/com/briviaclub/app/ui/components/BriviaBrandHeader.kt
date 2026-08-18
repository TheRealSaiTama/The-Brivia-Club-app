package com.briviaclub.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.briviaclub.app.ui.theme.AmberGold
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.EmeraldVerified

/**
 * High-fashion, luxury typography logo for "THE BRIVIA CLUB"
 */
@Composable
fun BriviaTypographyLogo(
    modifier: Modifier = Modifier,
    isDark: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer_transition")
    
    // Shimmering gold animation
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rot"
    )

    val goldGradient = Brush.linearGradient(
        colors = listOf(
            ChampagneGold,
            Color(0xFFFFF0D0),
            AmberGold,
            ChampagneGold
        ),
        start = Offset(shimmerOffset, 0f),
        end = Offset(shimmerOffset + 250f, 100f)
    )

    val brandGradient = if (isDark) {
        Brush.horizontalGradient(
            listOf(
                ChampagneGold,
                Color(0xFFFFF6E5),
                ChampagneGold
            )
        )
    } else {
        Brush.horizontalGradient(
            listOf(
                DeepWine,
                CommunityBadge,
                Color(0xFF9B1D3A)
            )
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Luxury Faceted Emblem with animated shimmer ring
        Box(
            modifier = Modifier
                .size(38.dp)
                .scale(pulseScale),
            contentAlignment = Alignment.Center
        ) {
            // Ambient glow
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                (if (isDark) ChampagneGold else DeepWine).copy(alpha = 0.35f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Diamond Gold Crest
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .rotate(45f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(DeepWine, CommunityBadge)
                        )
                    )
                    .border(
                        1.5.dp,
                        Brush.linearGradient(
                            listOf(ChampagneGold, Color(0xFFFFF3CD), AmberGold)
                        ),
                        RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Inner monogram 'B' in diamond
                Text(
                    text = "B",
                    color = ChampagneGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.rotate(-45f)
                )
            }
        }

        // Editorial Serif Typography: THE BRIVIA CLUB
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "THE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 3.sp,
                    color = if (isDark) ChampagneGold.copy(alpha = 0.85f) else DeepWine.copy(alpha = 0.75f)
                )
                Text(
                    text = "BRIVIA",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 4.5.sp,
                    color = if (isDark) ChampagneGold else DeepWine
                )
                Text(
                    text = "CLUB",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 3.sp,
                    color = if (isDark) ChampagneGold.copy(alpha = 0.85f) else CommunityBadge
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .clip(CircleShape)
                        .background(EmeraldVerified)
                )
                Text(
                    text = "PRIVATE BUILDER COLLECTIVE",
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.6.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }
        }
    }
}

/**
 * Top App Bar for Discovery & Main Screens with interactive animated action triggers
 */
@Composable
fun BriviaTopBar(
    onOpenFilters: () -> Unit,
    activeFilterCount: Int = 0,
    isDark: Boolean = false,
    onToggleTheme: (() -> Unit)? = null,
    onOpenNotifications: (() -> Unit)? = null,
    unreadNotificationsCount: Int = 0,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BriviaTypographyLogo(isDark = isDark)

        // Right side: Notifications, Theme Toggle, & Filter
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (onOpenNotifications != null) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            1.dp,
                            if (unreadNotificationsCount > 0) ChampagneGold else (if (isDark) ChampagneGold.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                            CircleShape
                        )
                        .clickable(onClick = onOpenNotifications),
                    contentAlignment = Alignment.Center
                ) {
                    if (unreadNotificationsCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = DeepWine,
                                    contentColor = Color.White,
                                    modifier = Modifier.size(15.dp)
                                ) {
                                    Text(
                                        text = if (unreadNotificationsCount > 9) "9+" else unreadNotificationsCount.toString(),
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.NotificationsActive,
                                contentDescription = "Push Notifications ($unreadNotificationsCount unread)",
                                tint = AmberGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = if (isDark) ChampagneGold else DeepWine,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            if (onToggleTheme != null) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            1.dp,
                            if (isDark) ChampagneGold.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            CircleShape
                        )
                        .clickable(onClick = onToggleTheme),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isDark) Icons.Default.Brightness7 else Icons.Default.Brightness4,
                        contentDescription = "Toggle Theme",
                        tint = if (isDark) ChampagneGold else DeepWine,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Live Guild Indicator
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (isDark) DeepWine.copy(alpha = 0.4f) else ChampagneGold.copy(alpha = 0.18f)
                    )
                    .border(
                        1.dp,
                        ChampagneGold.copy(alpha = 0.35f),
                        CircleShape
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(EmeraldVerified)
                    )
                    Text(
                        text = "CURATED",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        color = if (isDark) ChampagneGold else DeepWine
                    )
                }
            }

            // Filter Icon Button with animated badge
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(
                        1.dp,
                        if (activeFilterCount > 0) ChampagneGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        CircleShape
                    )
                    .clickable(onClick = onOpenFilters),
                contentAlignment = Alignment.Center
            ) {
                if (activeFilterCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = DeepWine,
                                contentColor = Color.White,
                                modifier = Modifier.size(14.dp)
                            ) {
                                Text("$activeFilterCount", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filters",
                            tint = if (isDark) ChampagneGold else DeepWine,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filters",
                        tint = if (isDark) ChampagneGold else DeepWine,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
