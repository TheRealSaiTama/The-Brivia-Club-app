package com.briviaclub.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.briviaclub.app.data.local.entity.ActivityEntity
import com.briviaclub.app.ui.theme.AmberGold
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.EmeraldVerified
import com.briviaclub.app.ui.theme.SuperLikeBlue
import com.briviaclub.app.ui.viewmodel.BriviaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ActivityFilterCategory(val label: String, val icon: ImageVector) {
    ALL("All Pulse", Icons.Default.FlashOn),
    MATCHES("Matches", Icons.Default.Handshake),
    SUPERLIKES("Super Likes", Icons.Default.Star),
    VIP("VIP Upgrades", Icons.Default.WorkspacePremium),
    PROJECTS("Milestones", Icons.Default.AutoAwesome)
}

@Composable
fun MemberActivityFeedComponent(
    viewModel: BriviaViewModel,
    onNavigateChat: ((matchId: String, name: String, initial: String, role: String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val activities by viewModel.activities.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    var selectedFilter by remember { mutableStateOf(ActivityFilterCategory.ALL) }
    var memberSearchQuery by remember { mutableStateOf("") }
    var isPostingExpanded by remember { mutableStateOf(false) }
    var postContent by remember { mutableStateOf("") }
    var selectedPostTag by remember { mutableStateOf("🚀 Building") }
    var isFeedLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isFeedLoaded = true
    }

    val haptic = LocalHapticFeedback.current
    val isDark = MaterialTheme.colorScheme.background.red < 0.2f

    val filteredActivities = remember(activities, selectedFilter, memberSearchQuery) {
        val baseList = when (selectedFilter) {
            ActivityFilterCategory.ALL -> activities
            ActivityFilterCategory.MATCHES -> activities.filter { it.actionType == "MATCH" }
            ActivityFilterCategory.SUPERLIKES -> activities.filter { it.actionType == "SUPERLIKE" }
            ActivityFilterCategory.VIP -> activities.filter { it.actionType == "TIER_UPGRADE" }
            ActivityFilterCategory.PROJECTS -> activities.filter { it.actionType in listOf("COLLAB_POST", "PROFILE_UPDATE", "SKILL_UPDATE") }
        }

        if (memberSearchQuery.isBlank()) {
            baseList
        } else {
            val q = memberSearchQuery.trim()
            baseList.filter { activity ->
                activity.actorName.contains(q, ignoreCase = true) ||
                    (activity.targetName?.contains(q, ignoreCase = true) == true) ||
                    activity.actorRole.contains(q, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Search Bar for Finding Members in Activity Feed
        OutlinedTextField(
            value = memberSearchQuery,
            onValueChange = { memberSearchQuery = it },
            placeholder = {
                Text(
                    "Search members by name in feed...",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search member by name",
                    tint = if (memberSearchQuery.isNotEmpty()) CommunityBadge else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (memberSearchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { memberSearchQuery = "" },
                        modifier = Modifier.testTag("clear_member_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = CircleShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = CommunityBadge,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("member_activity_search_bar")
        )

        // Live Pulse Header Banner
        LivePulseHeaderCard(
            totalEvents = activities.size,
            isDark = isDark,
            onQuickPostClick = { isPostingExpanded = !isPostingExpanded }
        )

        // Active Search Filter Indicator (if query active)
        if (memberSearchQuery.isNotBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(CommunityBadge.copy(alpha = 0.08f))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Showing results for \"$memberSearchQuery\" (${filteredActivities.size})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Clear filter",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CommunityBadge,
                    modifier = Modifier
                        .clickable { memberSearchQuery = "" }
                        .testTag("active_search_clear_pill")
                )
            }
        }

        // Post Builder Update Box (Expandable / Inline)
        AnimatedVisibility(
            visible = isPostingExpanded,
            enter = fadeIn(tween(250)) + slideInVertically(tween(250)) { -20 },
            exit = fadeOut(tween(200))
        ) {
            QuickPostBuilderCard(
                userAvatar = currentUser?.photoUrlsJson?.split(",")?.firstOrNull().orEmpty(),
                userName = currentUser?.name ?: "You",
                content = postContent,
                onContentChange = { postContent = it },
                selectedTag = selectedPostTag,
                onTagSelect = { selectedPostTag = it },
                onPost = {
                    if (postContent.isNotBlank()) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.postMemberUpdate(postContent, selectedPostTag)
                        postContent = ""
                        isPostingExpanded = false
                    }
                },
                onCancel = { isPostingExpanded = false },
                isDark = isDark
            )
        }

        // Filter chips bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActivityFilterCategory.entries.forEach { category ->
                val isSelected = selectedFilter == category
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        selectedFilter = category
                    },
                    label = {
                        Text(
                            text = category.label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CommunityBadge,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = MaterialTheme.colorScheme.outline,
                        selectedBorderColor = CommunityBadge,
                        borderWidth = 1.dp
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        // Activities Timeline List
        if (filteredActivities.isEmpty()) {
            EmptyActivityState(
                filter = selectedFilter,
                searchQuery = memberSearchQuery,
                onClearSearch = { memberSearchQuery = "" },
                onPostClick = { isPostingExpanded = true }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 90.dp, top = 2.dp)
            ) {
                itemsIndexed(filteredActivities, key = { _, activity -> activity.id }) { index, activity ->
                    AnimatedVisibility(
                        visible = isFeedLoaded,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 400,
                                delayMillis = (index * 60).coerceAtMost(480),
                                easing = FastOutSlowInEasing
                            )
                        ) + slideInVertically(
                            animationSpec = tween(
                                durationMillis = 450,
                                delayMillis = (index * 60).coerceAtMost(480),
                                easing = FastOutSlowInEasing
                            ),
                            initialOffsetY = { fullHeight -> fullHeight / 2 }
                        )
                    ) {
                        ActivityItemCard(
                            activity = activity,
                            onLikeClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                viewModel.toggleActivityLike(activity.id)
                            },
                            onActionClick = {
                                if (activity.targetId != null && onNavigateChat != null) {
                                    onNavigateChat(
                                        "match_${currentUser?.id}_${activity.targetId}",
                                        activity.targetName ?: "Builder",
                                        activity.targetName?.take(1) ?: "B",
                                        activity.actorRole
                                    )
                                }
                            },
                            isDark = isDark
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LivePulseHeaderCard(
    totalEvents: Int,
    isDark: Boolean,
    onQuickPostClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1715) else Color(0xFFFFF9F5)
        ),
        border = BorderStroke(1.dp, CommunityBadge.copy(alpha = 0.28f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Glowing Pulse Indicator
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF00E676).copy(alpha = pulseAlpha)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00C853))
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "LIVE CLUB PULSE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp,
                            color = CommunityBadge
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CommunityBadge.copy(alpha = 0.12f))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "$totalEvents updates",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Text(
                        text = "Real-time builder connections & milestones",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Quick Post Action Button
            Button(
                onClick = onQuickPostClick,
                colors = ButtonDefaults.buttonColors(containerColor = CommunityBadge),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Post",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun QuickPostBuilderCard(
    userAvatar: String,
    userName: String,
    content: String,
    onContentChange: (String) -> Unit,
    selectedTag: String,
    onTagSelect: (String) -> Unit,
    onPost: () -> Unit,
    onCancel: () -> Unit,
    isDark: Boolean
) {
    val postTags = listOf(
        "🚀 Building",
        "💡 Seeking Feedback",
        "🤝 Need Co-founder",
        "🎉 Milestone",
        "⭐ Collab Open"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, CommunityBadge.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (userAvatar.isNotBlank()) {
                        AsyncImage(
                            model = userAvatar,
                            contentDescription = userName,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .border(1.dp, CommunityBadge, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(CommunityBadge),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Y", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    Text(
                        text = "Share with Brivia Club",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "Cancel",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable { onCancel() }
                )
            }

            // Status input field
            OutlinedTextField(
                value = content,
                onValueChange = onContentChange,
                placeholder = {
                    Text(
                        "e.g. Shipped our Kotlin + AI SLM v1.0 prototype! Seeking beta testers & frontend partners...",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                },
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = if (isDark) Color(0xFF18181A) else Color(0xFFF9F9FB),
                    unfocusedContainerColor = if (isDark) Color(0xFF18181A) else Color(0xFFF9F9FB),
                    focusedBorderColor = CommunityBadge,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Tag selector chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(postTags) { tag ->
                    val isSelected = selectedTag == tag
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSelected) CommunityBadge else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { onTagSelect(tag) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = tag,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Post Button
            Button(
                onClick = onPost,
                enabled = content.trim().isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CommunityBadge,
                    disabledContainerColor = CommunityBadge.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Broadcast to Members",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ActivityItemCard(
    activity: ActivityEntity,
    onLikeClick: () -> Unit,
    onActionClick: () -> Unit,
    isDark: Boolean
) {
    val relativeTime = remember(activity.timestamp) {
        formatRelativeTime(activity.timestamp)
    }

    val badgeColor = when (activity.actionType) {
        "MATCH" -> Color(0xFF00C853)
        "SUPERLIKE" -> AmberGold
        "TIER_UPGRADE" -> DeepWine
        "COLLAB_POST" -> SuperLikeBlue
        else -> CommunityBadge
    }

    val cardBg = if (isDark) {
        Color(0xFF1E1E22)
    } else {
        Color(0xFFFFFFFF)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(
            1.dp,
            if (activity.actionType == "MATCH" || activity.actionType == "TIER_UPGRADE") {
                badgeColor.copy(alpha = 0.35f)
            } else {
                MaterialTheme.colorScheme.outline
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top Row: Actor Avatar, Name, Role, Badge & Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box {
                        if (activity.actorAvatarUrl.isNotBlank()) {
                            AsyncImage(
                                model = activity.actorAvatarUrl,
                                contentDescription = activity.actorName,
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, badgeColor, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(CommunityBadge),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = activity.actorName.take(1),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Type mini overlay badge
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(badgeColor)
                                .border(1.dp, MaterialTheme.colorScheme.surface, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            val miniIcon = when (activity.actionType) {
                                "MATCH" -> Icons.Default.Handshake
                                "SUPERLIKE" -> Icons.Default.Star
                                "TIER_UPGRADE" -> Icons.Default.WorkspacePremium
                                else -> Icons.Default.AutoAwesome
                            }
                            Icon(
                                imageVector = miniIcon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = activity.actorName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = EmeraldVerified,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = activity.actorRole,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Time & Badge text
                Column(horizontalAlignment = Alignment.End) {
                    if (!activity.badgeText.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(badgeColor.copy(alpha = 0.14f))
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = activity.badgeText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = badgeColor
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = relativeTime,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            }

            // Title & Description Body
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = activity.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = activity.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp
                )
            }

            // Nested Target Builder Card (For Mutual Matches & Superlikes)
            if (!activity.targetName.isNullOrBlank()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isDark) Color(0xFF2A2830) else Color(0xFFF3F1F8)
                        )
                        .clickable { onActionClick() }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (!activity.targetAvatarUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = activity.targetAvatarUrl,
                                contentDescription = activity.targetName,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(DeepWine),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = activity.targetName.take(1),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "Matched with ${activity.targetName}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Tap to open chat & collaborate",
                                fontSize = 10.sp,
                                color = CommunityBadge
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Chat",
                        tint = CommunityBadge,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Bottom Actions Bar (Cheer / React / Share)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // High Five / Like Action
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onLikeClick() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (activity.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (activity.isLikedByMe) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (activity.likesCount > 0) "${activity.likesCount} cheers" else "Cheer 🙌",
                        fontSize = 11.sp,
                        fontWeight = if (activity.isLikedByMe) FontWeight.Bold else FontWeight.Normal,
                        color = if (activity.isLikedByMe) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Action Prompt chip
                if (activity.actionType == "COLLAB_POST" || activity.actionType == "SKILL_UPDATE") {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CommunityBadge.copy(alpha = 0.08f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "💡 Club Discussion",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CommunityBadge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyActivityState(
    filter: ActivityFilterCategory,
    searchQuery: String = "",
    onClearSearch: () -> Unit = {},
    onPostClick: () -> Unit
) {
    val isSearching = searchQuery.isNotBlank()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(CommunityBadge.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isSearching) Icons.Default.Search else Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = CommunityBadge,
                    modifier = Modifier.size(30.dp)
                )
            }

            Text(
                text = if (isSearching) "No Members Found" else "No ${filter.label} Yet",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = if (isSearching) {
                    "No pulse updates or members found matching \"$searchQuery\". Try checking the spelling or resetting the search filter."
                } else {
                    "Be the catalyst! Post your current project or connect with builders to ignite the club timeline."
                },
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 18.sp
            )

            if (isSearching) {
                Button(
                    onClick = onClearSearch,
                    colors = ButtonDefaults.buttonColors(containerColor = CommunityBadge),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("empty_state_clear_search_btn")
                ) {
                    Text("Clear Search Query", fontWeight = FontWeight.Bold, color = Color.White)
                }
            } else {
                Button(
                    onClick = onPostClick,
                    colors = ButtonDefaults.buttonColors(containerColor = CommunityBadge),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Post First Update 🚀", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

fun formatRelativeTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "${minutes}m ago"
        hours < 24 -> "${hours}h ago"
        days < 7 -> "${days}d ago"
        else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(timestamp))
    }
}
