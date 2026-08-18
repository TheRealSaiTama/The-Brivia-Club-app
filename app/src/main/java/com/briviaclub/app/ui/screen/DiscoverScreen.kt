package com.briviaclub.app.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.briviaclub.app.data.local.entity.UserEntity
import com.briviaclub.app.ui.components.BriviaTopBar
import com.briviaclub.app.ui.components.MemberActivityFeedComponent
import com.briviaclub.app.ui.components.NotificationCenterSheet
import com.briviaclub.app.ui.theme.AmberGold
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.EmeraldVerified
import com.briviaclub.app.ui.theme.SuperLikeBlue
import com.briviaclub.app.ui.viewmodel.BriviaViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DiscoverScreen(
    viewModel: BriviaViewModel,
    onNavigateChat: (matchId: String, partnerName: String, partnerInitial: String, partnerRole: String) -> Unit,
    onNavigateUpgrade: () -> Unit
) {
    val feed by viewModel.filteredFeed.collectAsState()
    val filter by viewModel.currentFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val matchCelebration by viewModel.matchCelebration.collectAsState()
    val subscription by viewModel.subscription.collectAsState()
    val unreadNotificationsCount by viewModel.unreadNotificationsCount.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }
    var showNotificationCenter by remember { mutableStateOf(false) }
    var selectedProfileForDetail by remember { mutableStateOf<UserEntity?>(null) }
    var reportDialogUser by remember { mutableStateOf<UserEntity?>(null) }
    var showReportReasonDialog by remember { mutableStateOf(false) }
    var reportReason by remember { mutableStateOf("") }

    val filterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val detailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val isDark = MaterialTheme.colorScheme.background.red < 0.2f
    var discoverViewMode by remember { mutableStateOf("deck") } // "deck" or "pulse"

    // Current top card in deck
    val currentCard = feed.firstOrNull()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // Top Brand Header with Luxury Typography & Actions
            val activeFilterCount = (if (filter.locationFilter != "All locations") 1 else 0) +
                    (if (filter.categoryFilter != "All") 1 else 0) +
                    (if (filter.minMatchPercent > 60) 1 else 0)

            BriviaTopBar(
                onOpenFilters = { showFilterSheet = true },
                activeFilterCount = activeFilterCount,
                isDark = isDark,
                onToggleTheme = { viewModel.toggleDarkTheme() },
                onOpenNotifications = { showNotificationCenter = true },
                unreadNotificationsCount = unreadNotificationsCount
            )

            // Discover Mode Switcher (Deck vs Live Activity Feed)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isDark) Color(0xFF1E1E24) else Color(0xFFEFEFF4))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Deck Tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (discoverViewMode == "deck") CommunityBadge else Color.Transparent)
                        .clickable { discoverViewMode = "deck" }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎴 Discover Cards (${feed.size})",
                        fontSize = 12.sp,
                        fontWeight = if (discoverViewMode == "deck") FontWeight.Bold else FontWeight.Medium,
                        color = if (discoverViewMode == "deck") Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Pulse Tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (discoverViewMode == "pulse") CommunityBadge else Color.Transparent)
                        .clickable { discoverViewMode = "pulse" }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (discoverViewMode == "pulse") Color(0xFF00E676) else CommunityBadge)
                        )
                        Text(
                            text = "⚡ Member Feed",
                            fontSize = 12.sp,
                            fontWeight = if (discoverViewMode == "pulse") FontWeight.Bold else FontWeight.Medium,
                            color = if (discoverViewMode == "pulse") Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (discoverViewMode == "pulse") {
                // Render Full Member Activity Feed Component
                MemberActivityFeedComponent(
                    viewModel = viewModel,
                    onNavigateChat = onNavigateChat,
                    modifier = Modifier.weight(1f)
                )
            } else {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    placeholder = {
                        Text(
                            "Search by name, skill (e.g. AI, Rust), or city...",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(18.dp))
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
                )

                // Swipe Daily Limits indicator (for free tier)
                if (subscription?.planId == "free") {
                    val used = subscription?.dailySwipesUsed ?: 0
                    val remaining = (20 - used).coerceAtLeast(0)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CommunityBadge.copy(alpha = 0.08f))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🔥 $remaining free swipes left today",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Upgrade to Pro Unlimited →",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CommunityBadge,
                            modifier = Modifier.clickable { onNavigateUpgrade() }
                        )
                    }
                }

                // Main Deck Card Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentCard != null) {
                        val matchScore = viewModel.calculateMatchScore(currentCard)
                        InteractiveSwipeCard(
                            user = currentCard,
                            matchScore = matchScore,
                            onSwipeLeft = { viewModel.handleSwipe(currentCard, "pass") },
                            onSwipeRight = { viewModel.handleSwipe(currentCard, "like") },
                            onSuperLike = { viewModel.handleSwipe(currentCard, "superlike") },
                            onViewDetails = { selectedProfileForDetail = currentCard },
                            onReport = {
                                reportDialogUser = currentCard
                                showReportReasonDialog = true
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        // Empty State
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(CommunityBadge.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        tint = CommunityBadge,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }

                                Text(
                                    text = "You're all caught up!",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = "No more builders match your current filters. Broaden your location or refresh the club recommendations.",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 19.sp
                                )

                                Row(
                                    modifier = Modifier.padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.updateFilters(
                                                location = "All locations",
                                                category = "All",
                                                distance = 200,
                                                minMatch = 50,
                                                sortBy = "match_percent"
                                            )
                                            viewModel.refreshFeed()
                                        },
                                        shape = CircleShape
                                    ) {
                                        Text("Reset Filters", fontWeight = FontWeight.Bold)
                                    }

                                    Button(
                                        onClick = { viewModel.refreshFeed() },
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(containerColor = CommunityBadge)
                                    ) {
                                        Text("Refresh Deck", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                // Bottom Action Controls (Pass, Super Like, Connect, Rewind)
                if (currentCard != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Rewind / Refresh
                        IconButton(
                            onClick = { viewModel.refreshFeed() },
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Rewind", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Pass (X)
                        IconButton(
                            onClick = { viewModel.handleSwipe(currentCard, "pass") },
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .border(1.5.dp, Color(0xFFEF4444).copy(alpha = 0.6f), CircleShape)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Pass", tint = Color(0xFFEF4444), modifier = Modifier.size(26.dp))
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Super Like (Star)
                        IconButton(
                            onClick = { viewModel.handleSwipe(currentCard, "superlike") },
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(SuperLikeBlue.copy(alpha = 0.15f))
                                .border(1.5.dp, SuperLikeBlue, CircleShape)
                        ) {
                            Icon(Icons.Default.Star, contentDescription = "Super Like", tint = SuperLikeBlue, modifier = Modifier.size(24.dp))
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Connect / Like (Heart)
                        IconButton(
                            onClick = { viewModel.handleSwipe(currentCard, "like") },
                            modifier = Modifier
                                .size(58.dp)
                                .shadow(6.dp, CircleShape)
                                .clip(CircleShape)
                                .background(CommunityBadge)
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = "Connect", tint = Color.White, modifier = Modifier.size(28.dp))
                        }
                    }
                }
            }
        }

        // Match Celebration Modal Overlay
        matchCelebration?.let { matchedUser ->
            MatchCelebrationDialog(
                user = matchedUser,
                onDismiss = { viewModel.dismissMatchCelebration() },
                onSendMessage = {
                    val matchId = "match_me_builder_001_${matchedUser.id}"
                    viewModel.dismissMatchCelebration()
                    onNavigateChat(
                        matchId,
                        matchedUser.name,
                        matchedUser.name.take(1),
                        matchedUser.role
                    )
                },
                onViewProfile = {
                    viewModel.dismissMatchCelebration()
                    selectedProfileForDetail = matchedUser
                }
            )
        }

        // Full Profile Detail BottomSheet
        selectedProfileForDetail?.let { detailUser ->
            ModalBottomSheet(
                onDismissRequest = { selectedProfileForDetail = null },
                sheetState = detailSheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                FullProfileBottomSheetContent(
                    user = detailUser,
                    matchScore = viewModel.calculateMatchScore(detailUser),
                    onClose = { selectedProfileForDetail = null },
                    onConnect = {
                        viewModel.handleSwipe(detailUser, "like")
                        selectedProfileForDetail = null
                    },
                    onReport = {
                        reportDialogUser = detailUser
                        showReportReasonDialog = true
                        selectedProfileForDetail = null
                    }
                )
            }
        }

        // Filter BottomSheet
        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                sheetState = filterSheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                FilterBottomSheetContent(
                    currentFilter = filter,
                    onApply = { loc, cat, dist, minM, sort ->
                        viewModel.updateFilters(loc, cat, dist, minM, sort)
                        showFilterSheet = false
                    },
                    onClose = { showFilterSheet = false }
                )
            }
        }

        // Report / Block Dialog
        if (showReportReasonDialog && reportDialogUser != null) {
            Dialog(onDismissRequest = { showReportReasonDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Report / Block Builder",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Tell us what happened with ${reportDialogUser?.name}. We review all community flags within 2 hours.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        OutlinedTextField(
                            value = reportReason,
                            onValueChange = { reportReason = it },
                            label = { Text("Reason (Spam, Harassment, Fake Profile)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    reportDialogUser?.let { viewModel.blockUser(it.id, reportReason.ifBlank { "User Blocked" }) }
                                    showReportReasonDialog = false
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Block User", color = Color(0xFFEF4444))
                            }

                            Button(
                                onClick = {
                                    reportDialogUser?.let { viewModel.reportUser(it.id, reportReason.ifBlank { "Flagged Report" }) }
                                    showReportReasonDialog = false
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CommunityBadge)
                            ) {
                                Text("Submit Report")
                            }
                        }
                    }
                }
            }
        }

        // Notification Center Sheet
        if (showNotificationCenter) {
            NotificationCenterSheet(
                viewModel = viewModel,
                onDismiss = { showNotificationCenter = false },
                onNavigateToDestination = { dest ->
                    showNotificationCenter = false
                    if (dest == "feed") {
                        discoverViewMode = "pulse"
                    } else if (dest == "membership") {
                        onNavigateUpgrade()
                    }
                }
            )
        }
    }
}

@Composable
private fun InteractiveSwipeCard(
    user: UserEntity,
    matchScore: Int,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onSuperLike: () -> Unit,
    onViewDetails: () -> Unit,
    onReport: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    Box(
        modifier = modifier
            .padding(vertical = 4.dp)
            .pointerInput(user.id) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                            offsetY.snapTo(offsetY.value + dragAmount.y)
                        }
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            if (offsetX.value > 220f) {
                                onSwipeRight()
                                offsetX.snapTo(0f)
                                offsetY.snapTo(0f)
                            } else if (offsetX.value < -220f) {
                                onSwipeLeft()
                                offsetX.snapTo(0f)
                                offsetY.snapTo(0f)
                            } else if (offsetY.value < -200f) {
                                onSuperLike()
                                offsetX.snapTo(0f)
                                offsetY.snapTo(0f)
                            } else {
                                offsetX.animateTo(0f, spring())
                                offsetY.animateTo(0f, spring())
                            }
                        }
                    }
                )
            }
            .graphicsLayer {
                translationX = offsetX.value
                translationY = offsetY.value
                rotationZ = (offsetX.value / 25f).coerceIn(-18f, 18f)
            }
    ) {
        // Stack depth illusion back shadow
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
                .offset(y = 12.dp),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFDCD6D2).copy(alpha = 0.5f))
        ) {}

        // Main Front Card
        Card(
            modifier = Modifier
                .fillMaxSize()
                .shadow(16.dp, RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = DeepWine)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Background Image
                AsyncImage(
                    model = user.photoUrlsJson.ifBlank { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800" },
                    contentDescription = user.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient scrim overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.35f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.88f)
                                )
                            )
                        )
                )

                // Live Dynamic Swipe Stamp Badges
                val rightAlpha = (offsetX.value / 120f).coerceIn(0f, 1f)
                val leftAlpha = (-offsetX.value / 120f).coerceIn(0f, 1f)
                val superAlpha = (-offsetY.value / 100f).coerceIn(0f, 1f)

                if (rightAlpha > 0.05f) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 24.dp, top = 80.dp)
                            .rotate(-18f)
                            .border(3.dp, EmeraldVerified.copy(alpha = rightAlpha), RoundedCornerShape(12.dp))
                            .background(EmeraldVerified.copy(alpha = rightAlpha * 0.25f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "CONNECT",
                            color = Color.White.copy(alpha = rightAlpha),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                    }
                }

                if (leftAlpha > 0.05f) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 24.dp, top = 80.dp)
                            .rotate(18f)
                            .border(3.dp, Color(0xFFEF4444).copy(alpha = leftAlpha), RoundedCornerShape(12.dp))
                            .background(Color(0xFFEF4444).copy(alpha = leftAlpha * 0.25f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "PASS",
                            color = Color.White.copy(alpha = leftAlpha),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                    }
                }

                if (superAlpha > 0.05f) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .border(3.dp, SuperLikeBlue.copy(alpha = superAlpha), RoundedCornerShape(14.dp))
                            .background(SuperLikeBlue.copy(alpha = superAlpha * 0.3f), RoundedCornerShape(14.dp))
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "⭐ SUPER PITCH",
                            color = Color.White.copy(alpha = superAlpha),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                    }
                }

                // Top Tag Ribbons (Match %, Verified Builder, Report button)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(EmeraldVerified)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "$matchScore% Match",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (user.isVerified) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.25f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = AmberGold,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Verified",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = onReport,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.35f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Flag,
                                contentDescription = "Report",
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Bottom Profile Summary Section
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "${user.name}, ${user.age}",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldVerified)
                                )
                            }

                            Text(
                                text = user.role,
                                fontSize = 15.sp,
                                color = Color.White.copy(alpha = 0.92f),
                                fontWeight = FontWeight.Medium
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.75f),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${user.location} • ${user.experienceYears}y exp",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.75f)
                                )
                            }
                        }

                        IconButton(
                            onClick = onViewDetails,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.25f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "View Details",
                                tint = Color.White
                            )
                        }
                    }

                    // Skills Tags Preview
                    val skills = user.skillsJson.split(",").take(3)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        skills.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = tag.trim(),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Tap to expand bar
                    Surface(
                        onClick = onViewDetails,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.16f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "View full bio & stack pitch",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchCelebrationDialog(
    user: UserEntity,
    onDismiss: () -> Unit,
    onSendMessage: () -> Unit,
    onViewProfile: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.82f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clickable(enabled = false, onClick = {}),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(CommunityBadge.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Match",
                            tint = CommunityBadge,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Text(
                        text = "It's a Build Match!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "You and ${user.name} both liked each other's vision. Start a conversation to explore shipping together.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = onSendMessage,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CommunityBadge,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Send First Pitch", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onViewProfile,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("View Profile", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FullProfileBottomSheetContent(
    user: UserEntity,
    matchScore: Int,
    onClose: () -> Unit,
    onConnect: () -> Unit,
    onReport: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${user.name}, ${user.age}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (user.isVerified) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = AmberGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    text = user.headline.ifBlank { user.role },
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Close")
            }
        }

        Divider()

        // Match Fit Stats
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Skill Compatibility", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("$matchScore%", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = CommunityBadge)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Experience", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${user.experienceYears} Years", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }

        // Bio Section
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("About & Vision", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(
                text = user.bio.ifBlank { "Passionate about building scalable products and collaborating with driven co-founders." },
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
            )
        }

        // Expertise Chips
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Skills & Expertise", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                user.skillsJson.split(",").forEach { skill ->
                    if (skill.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(text = skill.trim(), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }

        // Looking For Chips
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Looking For", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                user.lookingForJson.split(",").forEach { item ->
                    if (item.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(ChampagneGold.copy(alpha = 0.15f))
                                .border(1.dp, ChampagneGold.copy(alpha = 0.5f), CircleShape)
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(text = "🎯 ${item.trim()}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onReport,
                modifier = Modifier.weight(1f),
                shape = CircleShape
            ) {
                Text("Report Builder")
            }

            Button(
                onClick = onConnect,
                modifier = Modifier.weight(1.5f),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = CommunityBadge)
            ) {
                Text("Connect (Like)", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun FilterBottomSheetContent(
    currentFilter: com.briviaclub.app.data.local.entity.UserFilterEntity,
    onApply: (location: String, category: String, distance: Int, minMatch: Int, sortBy: String) -> Unit,
    onClose: () -> Unit
) {
    var selectedLocation by remember { mutableStateOf(currentFilter.locationFilter) }
    var selectedCategory by remember { mutableStateOf(currentFilter.categoryFilter) }
    var customLocationText by remember { mutableStateOf("") }
    var customCategoryText by remember { mutableStateOf("") }
    var distanceKm by remember { mutableStateOf(currentFilter.maxDistanceKm.toFloat()) }
    var minMatchPercent by remember { mutableStateOf(currentFilter.minMatchPercent.toFloat()) }
    var selectedSortBy by remember { mutableStateOf(currentFilter.sortBy) }

    val presetLocations = remember {
        mutableStateListOf(
            "All locations", "Bengaluru", "Pune", "Mumbai", "Delhi NCR", "San Francisco", "London", "Tokyo", "Remote"
        )
    }

    val presetCategories = remember {
        mutableStateListOf(
            "All", "Co-founder", "AI / ML", "Fullstack", "SaaS", "Growth", "UI/UX", "Web3", "Hackathon"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 36.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Discovery Filters",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            TextButton(
                onClick = {
                    selectedLocation = "All locations"
                    selectedCategory = "All"
                    distanceKm = 100f
                    minMatchPercent = 60f
                    selectedSortBy = "match_percent"
                }
            ) {
                Text("Reset All", color = CommunityBadge, fontWeight = FontWeight.Bold)
            }
        }

        // Location Filter with "Other / Custom City" support
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Location", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                presetLocations.forEach { loc ->
                    val isSelected = selectedLocation == loc
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedLocation = loc },
                        label = { Text(loc, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CommunityBadge,
                            selectedLabelColor = Color.White
                        ),
                        shape = CircleShape
                    )
                }
            }

            // Custom City input field
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = customLocationText,
                    onValueChange = { customLocationText = it },
                    placeholder = { Text("Add custom city...", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                Button(
                    onClick = {
                        val text = customLocationText.trim()
                        if (text.isNotEmpty() && !presetLocations.contains(text)) {
                            presetLocations.add(text)
                            selectedLocation = text
                            customLocationText = ""
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CommunityBadge)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }

        // Category / Focus Filter with "Other / Custom Tag" support
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Focus / Skills Category", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                presetCategories.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CommunityBadge,
                            selectedLabelColor = Color.White
                        ),
                        shape = CircleShape
                    )
                }
            }

            // Custom tag creator
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = customCategoryText,
                    onValueChange = { customCategoryText = it },
                    placeholder = { Text("Add custom skill/tag...", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                Button(
                    onClick = {
                        val text = customCategoryText.trim()
                        if (text.isNotEmpty() && !presetCategories.contains(text)) {
                            presetCategories.add(text)
                            selectedCategory = text
                            customCategoryText = ""
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CommunityBadge)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }

        // Distance Slider
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Max Distance", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("${distanceKm.roundToInt()} km", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CommunityBadge)
            }
            Slider(
                value = distanceKm,
                onValueChange = { distanceKm = it },
                valueRange = 10f..500f,
                colors = SliderDefaults.colors(
                    thumbColor = CommunityBadge,
                    activeTrackColor = CommunityBadge
                )
            )
        }

        // Min Match % Slider
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Minimum Match %", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("${minMatchPercent.roundToInt()}%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EmeraldVerified)
            }
            Slider(
                value = minMatchPercent,
                onValueChange = { minMatchPercent = it },
                valueRange = 50f..95f,
                colors = SliderDefaults.colors(
                    thumbColor = EmeraldVerified,
                    activeTrackColor = EmeraldVerified
                )
            )
        }

        // Sort By
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Sort Decks By", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            val sorts = listOf(
                "match_percent" to "Highest Match %",
                "recent" to "Recently Active",
                "experience" to "Years of Experience"
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                sorts.forEach { (key, label) ->
                    val isSelected = selectedSortBy == key
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedSortBy = key },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CommunityBadge,
                            selectedLabelColor = Color.White
                        ),
                        shape = CircleShape
                    )
                }
            }
        }

        Button(
            onClick = {
                onApply(
                    selectedLocation,
                    selectedCategory,
                    distanceKm.roundToInt(),
                    minMatchPercent.roundToInt(),
                    selectedSortBy
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = CommunityBadge)
        ) {
            Text("Apply Filters", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
