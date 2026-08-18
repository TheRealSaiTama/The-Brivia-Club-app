package com.briviaclub.app.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DynamicFeed
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.briviaclub.app.ui.components.BriviaTypographyLogo
import com.briviaclub.app.ui.components.DigitalMembershipCard
import com.briviaclub.app.ui.components.FullDigitalMembershipPassDialog
import com.briviaclub.app.ui.components.MembershipFaqSection
import com.briviaclub.app.ui.components.MembershipTier
import com.briviaclub.app.ui.components.MembershipTierCardsSection
import com.briviaclub.app.ui.components.NotificationBellButton
import com.briviaclub.app.ui.components.NotificationCenterSheet
import com.briviaclub.app.ui.theme.AmberGold
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.EmeraldVerified
import com.briviaclub.app.ui.viewmodel.BriviaViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    viewModel: BriviaViewModel,
    onNavigateUpgrade: () -> Unit,
    onNavigateOnboarding: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val subscription by viewModel.subscription.collectAsState()
    val isDark by viewModel.isDarkTheme.collectAsState()
    val feedAlertsEnabled by viewModel.feedAlertsEnabled.collectAsState()
    val membershipAlertsEnabled by viewModel.membershipAlertsEnabled.collectAsState()
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isEditing by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showNotificationCenter by remember { mutableStateOf(false) }
    var showFullPassDialog by remember { mutableStateOf(false) }

    // Edit form states
    var name by remember(currentUser) { mutableStateOf(currentUser?.name ?: "") }
    var role by remember(currentUser) { mutableStateOf(currentUser?.role ?: "") }
    var headline by remember(currentUser) { mutableStateOf(currentUser?.headline ?: "") }
    var bio by remember(currentUser) { mutableStateOf(currentUser?.bio ?: "") }
    var location by remember(currentUser) { mutableStateOf(currentUser?.location ?: "Bengaluru") }
    var age by remember(currentUser) { mutableStateOf((currentUser?.age ?: 26).toFloat()) }
    var experienceYears by remember(currentUser) { mutableStateOf((currentUser?.experienceYears ?: 4).toFloat()) }
    var photoUrl by remember(currentUser) { mutableStateOf(currentUser?.photoUrlsJson ?: "") }
    var isVisible by remember(currentUser) { mutableStateOf(currentUser?.isVisible ?: true) }

    // Validation state for display name and bio
    var nameError by remember { mutableStateOf<String?>(null) }
    var bioError by remember { mutableStateOf<String?>(null) }

    // Skills & Looking for with custom chip creation
    val selectedSkills = remember(currentUser) {
        mutableStateListOf<String>().apply {
            currentUser?.skillsJson?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }?.let { addAll(it) }
        }
    }
    var customSkillInput by remember { mutableStateOf("") }

    val selectedLookingFor = remember(currentUser) {
        mutableStateListOf<String>().apply {
            currentUser?.lookingForJson?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }?.let { addAll(it) }
        }
    }
    var customLookingForInput by remember { mutableStateOf("") }

    val avatarPresets = listOf(
        "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=800",
        "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=800",
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800",
        "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=800"
    )

    fun validateAndSave(): Boolean {
        var isValid = true

        if (name.trim().isBlank()) {
            nameError = "Display name cannot be empty"
            isValid = false
        } else {
            nameError = null
        }

        if (bio.trim().isBlank()) {
            bioError = "Bio cannot be empty"
            isValid = false
        } else {
            bioError = null
        }

        if (isValid) {
            viewModel.updateProfile(
                name = name.trim(),
                role = role.trim().ifBlank { "Founder & Builder" },
                headline = headline.trim(),
                bio = bio.trim(),
                location = location.trim().ifBlank { "Bengaluru" },
                age = age.roundToInt(),
                experienceYears = experienceYears.roundToInt(),
                photoUrl = photoUrl,
                skills = selectedSkills,
                lookingFor = selectedLookingFor,
                isVisible = isVisible
            )
            isEditing = false
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Profile updated successfully!")
            }
            return true
        } else {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Please correct the errors in the required fields.")
            }
            return false
        }
    }

    // Current tier info calculation (Bronze, Silver, Gold)
    val planId = subscription?.planId ?: if (currentUser?.isPremium == true) "pro" else "free"
    
    val tierTitle = when (planId) {
        "founder_vip" -> "Gold Founder VIP"
        "pro" -> "Silver Pro Builder"
        else -> "Bronze Member"
    }

    val tierBadgeLabel = when (planId) {
        "founder_vip" -> "GOLD VIP"
        "pro" -> "SILVER PRO"
        else -> "BRONZE TIER"
    }

    val tierBadgeEmoji = when (planId) {
        "founder_vip" -> "🥇"
        "pro" -> "🥈"
        else -> "🥉"
    }

    val tierStatusText = when (planId) {
        "founder_vip" -> "Gold Tier • 5x Priority & Private Mixers"
        "pro" -> "Silver Tier • Unlimited Swipes & 2x Boost"
        else -> "Bronze Tier • Standard Club Access"
    }

    val tierRankLevel = when (planId) {
        "founder_vip" -> "Tier Level 3 of 3 (Elite Max)"
        "pro" -> "Tier Level 2 of 3 (Pro Member)"
        else -> "Tier Level 1 of 3 (Standard)"
    }

    val tierProgress = when (planId) {
        "founder_vip" -> 1.0f
        "pro" -> 0.66f
        else -> 0.33f
    }

    val tierBadgeColor = when (planId) {
        "founder_vip" -> ChampagneGold
        "pro" -> Color(0xFF94A3B8)
        else -> Color(0xFFCD7F32)
    }

    val tierGradients = when (planId) {
        "founder_vip" -> listOf(ChampagneGold, Color(0xFFFFF2BF), AmberGold)
        "pro" -> listOf(Color(0xFF94A3B8), Color(0xFFF1F5F9), Color(0xFF64748B))
        else -> listOf(Color(0xFFB87333), Color(0xFFE5A869), Color(0xFF8C4C14))
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    Snackbar(
                        snackbarData = snackbarData,
                        containerColor = if (isDark) Color(0xFF1E2230) else DeepWine,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .systemBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // Top Actions Bar with Dynamic Tier Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BriviaTypographyLogo(isDark = isDark)

                    // Dynamic Tier Indicator Badge in Top Bar
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Brush.horizontalGradient(tierGradients.map { it.copy(alpha = 0.18f) }))
                            .border(1.dp, Brush.horizontalGradient(tierGradients), CircleShape)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onNavigateUpgrade()
                            }
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(tierBadgeEmoji, fontSize = 10.sp)
                            Text(
                                text = tierBadgeLabel,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp,
                                color = if (isDark) Color.White else tierBadgeColor
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    NotificationBellButton(
                        viewModel = viewModel,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            showNotificationCenter = true
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    )

                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            viewModel.toggleDarkTheme()
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Brightness4,
                            contentDescription = "Theme",
                            tint = if (isDark) ChampagneGold else DeepWine,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Button(
                        onClick = {
                            if (isEditing) {
                                validateAndSave()
                            } else {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                isEditing = true
                            }
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEditing) EmeraldVerified else ChampagneGold,
                            contentColor = if (isEditing) Color.White else Color.Black
                        ),
                        modifier = Modifier.testTag(if (isEditing) "save_profile_button" else "edit_profile_button")
                    ) {
                        Icon(
                            imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isEditing) "Save Profile" else "Edit Profile", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Membership Tier Status Showcase Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onNavigateUpgrade()
                    },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (planId == "free") MaterialTheme.colorScheme.surface else Color(0xFF141620)
                ),
                border = BorderStroke(1.5.dp, Brush.horizontalGradient(tierGradients)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Subtle background radial gradient for metallic shimmer
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(tierBadgeColor.copy(alpha = 0.25f), Color.Transparent),
                                    radius = 450f
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(tierGradients)
                                        )
                                        .border(1.5.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = tierBadgeEmoji,
                                        fontSize = 20.sp
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "MEMBERSHIP STATUS",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            letterSpacing = 1.5.sp,
                                            color = if (planId != "free") ChampagneGold else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = tierTitle,
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (planId != "free") Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = tierStatusText,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (planId != "free") Color(0xFFBAC5D6) else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Dynamic Status Badge Pill
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(
                                        Brush.horizontalGradient(tierGradients.map { it.copy(alpha = 0.25f) })
                                    )
                                    .border(
                                        1.dp,
                                        Brush.horizontalGradient(tierGradients),
                                        CircleShape
                                    )
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = if (planId == "founder_vip") "GOLD ELITE" else if (planId == "pro") "SILVER ACTIVE" else "BRONZE TIER",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp,
                                    color = if (planId != "free") Color.White else tierBadgeColor
                                )
                            }
                        }

                        // Tier Progress Indicator Bar
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = tierRankLevel,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (planId != "free") ChampagneGold else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${(tierProgress * 100).toInt()}% Unlocked",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = tierBadgeColor
                                )
                            }

                            LinearProgressIndicator(
                                progress = tierProgress,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = tierBadgeColor,
                                trackColor = if (isDark) Color(0xFF22283A) else Color(0xFFE2E8F0)
                            )
                        }

                        // Tier Benefits row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val perks = when (planId) {
                                "founder_vip" -> listOf("5x Boost", "20 Super Likes", "Private Mixers", "Pitch Deck Inbox")
                                "pro" -> listOf("2x Boost", "5 Super Likes", "Unlimited Swipes", "Tech Filters")
                                else -> listOf("1 Super Like/day", "Standard Feed", "Direct Chat")
                            }

                            perks.take(3).forEach { perk ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (planId != "free") Color(0xFF222533)
                                            else MaterialTheme.colorScheme.surfaceVariant
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "✓ $perk",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (planId != "free") Color(0xFFE2E8F0) else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (planId == "founder_vip") "Highest Priority Builder Access" else if (planId == "pro") "Upgrade to Gold VIP for 5x Boost" else "Upgrade to Silver or Gold from ₹999/mo",
                                fontSize = 12.sp,
                                color = ChampagneGold,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (planId == "founder_vip") "Manage Perks →" else "Upgrade Tier →",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ChampagneGold
                            )
                        }
                    }
                }
            }

            // Digital Membership Card with Scannable QR Code for Event Check-In
            currentUser?.let { user ->
                DigitalMembershipCard(
                    user = user,
                    planId = planId,
                    onExpandClick = { showFullPassDialog = true }
                )
            }

            // Visual Membership Tiers (Gold, Silver, Bronze) Cards
            MembershipTierCardsSection(
                currentPlanId = planId,
                onSelectTier = { tier ->
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    if (tier.id != planId) {
                        onNavigateUpgrade()
                    }
                }
            )

            // Profile Card Header Preview
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar with Dynamic Tier Metallic Border
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .border(3.dp, Brush.sweepGradient(tierGradients), CircleShape)
                                .padding(3.dp)
                        ) {
                            AsyncImage(
                                model = (if (isEditing) photoUrl else currentUser?.photoUrlsJson)?.ifBlank {
                                    "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=800"
                                },
                                contentDescription = "Profile Photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = if (isEditing) name else (currentUser?.name ?: "Aarav Sharma"),
                                    fontSize = 21.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified Member",
                                    tint = AmberGold,
                                    modifier = Modifier.size(18.dp)
                                )

                                // Dynamic Tier Pill Badge Beside Name
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Brush.horizontalGradient(tierGradients.map { it.copy(alpha = 0.2f) }))
                                        .border(1.dp, Brush.horizontalGradient(tierGradients), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Text(tierBadgeEmoji, fontSize = 9.sp)
                                        Text(
                                            text = tierBadgeLabel,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 0.5.sp,
                                            color = if (isDark) Color.White else tierBadgeColor
                                        )
                                    }
                                }
                            }

                            Text(
                                text = if (isEditing) role else (currentUser?.role ?: "Founding Engineer & AI Hacker"),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ChampagneGold
                            )

                            // Tier Status Text
                            Text(
                                text = "Status: $tierStatusText",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${if (isEditing) location else currentUser?.location} • ${if (isEditing) experienceYears.roundToInt() else currentUser?.experienceYears}y exp • Age ${if (isEditing) age.roundToInt() else currentUser?.age}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Views & Visibility Stats
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Profile Deck Impressions", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${currentUser?.viewsCount ?: 64} Profile Views this week", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (isVisible) EmeraldVerified.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = if (isVisible) "Active in Feed" else "Hidden",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isVisible) EmeraldVerified else Color.Gray
                            )
                        }
                    }
                }
            }

            if (!isEditing) {
                // Read Only Details View
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Member Bio & Headline
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Headline & Focus", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text(
                                text = currentUser?.headline?.ifBlank { "Building Autonomous Agents & Edge Mobile AI" } ?: "",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AmberGold
                            )

                            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                            Text("Member Biography", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text(
                                text = currentUser?.bio?.ifBlank { "Full-stack developer focused on Kotlin, Jetpack Compose, Python, and local AI LLM pipelines. Previously built scale distributed systems. Looking to collaborate on ambitious frontier tech ventures." } ?: "",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    // Skills Tags
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Core Expertise & Tech Stack", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                currentUser?.skillsJson?.split(",")?.forEach { skill ->
                                    if (skill.isNotBlank()) {
                                        Box(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text(skill.trim(), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Looking For Tags
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Looking For in the Club", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                currentUser?.lookingForJson?.split(",")?.forEach { item ->
                                    if (item.isNotBlank()) {
                                        Box(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .background(ChampagneGold.copy(alpha = 0.15f))
                                                .border(1.dp, ChampagneGold.copy(alpha = 0.5f), CircleShape)
                                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text("🎯 ${item.trim()}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Push Notification Settings & Test Alerts Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
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
                                    Icon(
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        tint = AmberGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Push Notifications & Alerts",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                TextButton(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        showNotificationCenter = true
                                    }
                                ) {
                                    Text("Open Center", fontSize = 12.sp, color = CommunityBadge, fontWeight = FontWeight.Bold)
                                }
                            }

                            Text(
                                text = "Receive real-time push alerts for high-value activity in your founder network and instant updates when your membership tier or perks change.",
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // Switch 1: Feed Activity Alerts
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(CommunityBadge.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.DynamicFeed,
                                            contentDescription = null,
                                            tint = CommunityBadge,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Feed & Member Activity",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Milestones, collaboration calls, and member updates",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Switch(
                                    checked = feedAlertsEnabled,
                                    onCheckedChange = {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        viewModel.setFeedAlertsEnabled(it)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = CommunityBadge,
                                        checkedTrackColor = CommunityBadge.copy(alpha = 0.4f)
                                    )
                                )
                            }

                            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                            // Switch 2: Membership Tier Updates
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(AmberGold.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.CardMembership,
                                            contentDescription = null,
                                            tint = AmberGold,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Membership & Tier Status",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Gold/Silver status upgrades & perk refreshes",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Switch(
                                    checked = membershipAlertsEnabled,
                                    onCheckedChange = {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        viewModel.setMembershipAlertsEnabled(it)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = AmberGold,
                                        checkedTrackColor = AmberGold.copy(alpha = 0.4f)
                                    )
                                )
                            }

                            // Interactive Test Buttons Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        viewModel.triggerTestFeedNotification()
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("🚀 Feed Activity alert sent!")
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text("Test Feed Alert", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        viewModel.triggerTestMembershipNotification()
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("👑 Membership Status alert sent!")
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text("Test VIP Alert", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Display Theme & Low-Light Contrast Mode Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
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
                                    Icon(
                                        imageVector = Icons.Default.Palette,
                                        contentDescription = null,
                                        tint = ChampagneGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Display & Ambient Contrast",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(if (isDark) DeepWine.copy(alpha = 0.35f) else ChampagneGold.copy(alpha = 0.15f))
                                        .border(1.dp, if (isDark) ChampagneGold else DeepWine.copy(alpha = 0.4f), CircleShape)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if (isDark) "LOW-LIGHT NOIR" else "PORCELAIN DAY",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 0.8.sp,
                                        color = if (isDark) ChampagneGold else DeepWine
                                    )
                                }
                            }

                            Text(
                                text = "Choose your reading mode. Dark mode delivers pure Obsidian contrast optimized for low-light environments while preserving the club's signature serif typography aesthetic.",
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // Interactive Mode Cards
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Dark Mode Option
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFF121620))
                                        .border(
                                            if (isDark) 2.dp else 1.dp,
                                            if (isDark) ChampagneGold else Color(0xFF2E3547),
                                            RoundedCornerShape(14.dp)
                                        )
                                        .clickable {
                                            if (!isDark) {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                viewModel.toggleDarkTheme()
                                            }
                                        }
                                        .padding(14.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Brightness4,
                                                contentDescription = null,
                                                tint = ChampagneGold,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            if (isDark) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = ChampagneGold,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = "Obsidian Dark",
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Max low-light contrast",
                                            color = Color(0xFFBAC5D6),
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                // Light Mode Option
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFFF8F9FA))
                                        .border(
                                            if (!isDark) 2.dp else 1.dp,
                                            if (!isDark) DeepWine else Color(0xFFE2E8F0),
                                            RoundedCornerShape(14.dp)
                                        )
                                        .clickable {
                                            if (isDark) {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                viewModel.toggleDarkTheme()
                                            }
                                        }
                                        .padding(14.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Brightness7,
                                                contentDescription = null,
                                                tint = DeepWine,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            if (!isDark) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = DeepWine,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = "Porcelain Light",
                                            color = Color(0xFF0F172A),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Crisp day elegance",
                                            color = Color(0xFF64748B),
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Accordion Membership FAQ Section
                    MembershipFaqSection(
                        isDark = isDark,
                        onContactConcierge = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("🛎️ Concierge desk notified! We'll reply in Club Messages.")
                            }
                        }
                    )

                    // Account Actions (Sign Out & Delete)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onNavigateOnboarding()
                            },
                            modifier = Modifier.weight(1f),
                            shape = CircleShape
                        ) {
                            Text("Sign Out")
                        }

                        OutlinedButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                showDeleteConfirmDialog = true
                            },
                            modifier = Modifier.weight(1f),
                            shape = CircleShape,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
                        ) {
                            Text("Delete Account")
                        }
                    }
                }
            } else {
                // Edit Profile Form
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Edit Member Profile",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Update your public founder card details (* indicates required field)",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Validation Alert Banner if errors exist
                    if (nameError != null || bioError != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f)),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = "Validation Error",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "Please fix required fields:",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    if (nameError != null) {
                                        Text("• $nameError", fontSize = 11.sp, color = MaterialTheme.colorScheme.onErrorContainer)
                                    }
                                    if (bioError != null) {
                                        Text("• $bioError", fontSize = 11.sp, color = MaterialTheme.colorScheme.onErrorContainer)
                                    }
                                }
                            }
                        }
                    }

                    // Avatar Presets Selector
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Select Profile Photo Preset", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            avatarPresets.forEach { url ->
                                val isSelected = photoUrl == url
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .border(if (isSelected) 3.dp else 1.dp, if (isSelected) ChampagneGold else Color.Transparent, CircleShape)
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            photoUrl = url
                                        }
                                ) {
                                    AsyncImage(
                                        model = url,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }

                    // Display Name (Required with validation)
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            if (it.trim().isNotBlank()) {
                                nameError = null
                            }
                        },
                        label = { Text("Display Name *") },
                        placeholder = { Text("Enter your full name or moniker") },
                        isError = nameError != null,
                        supportingText = {
                            if (nameError != null) {
                                Text(
                                    text = nameError!!,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.testTag("name_error_text")
                                )
                            } else {
                                Text("Your visible member name in club decks (Required)")
                            }
                        },
                        trailingIcon = {
                            if (nameError != null) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = "Error",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("name_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = role,
                        onValueChange = { role = it },
                        label = { Text("Role / Specialty (e.g. AI Researcher, Founder)") },
                        placeholder = { Text("Founding Engineer, AI Researcher, Venture Partner") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = headline,
                        onValueChange = { headline = it },
                        label = { Text("Headline / Elevator Pitch") },
                        placeholder = { Text("Building Autonomous Agents & Edge Mobile AI") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    // Member Bio (Required with validation)
                    OutlinedTextField(
                        value = bio,
                        onValueChange = {
                            bio = it
                            if (it.trim().isNotBlank()) {
                                bioError = null
                            }
                        },
                        label = { Text("Member Biography *") },
                        placeholder = { Text("Share what you're passionate about, your tech stack, and what you're building...") },
                        isError = bioError != null,
                        supportingText = {
                            if (bioError != null) {
                                Text(
                                    text = bioError!!,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.testTag("bio_error_text")
                                )
                            } else {
                                Text("${bio.length} characters • Minimum details required for match algorithm")
                            }
                        },
                        trailingIcon = {
                            if (bioError != null) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = "Error",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("bio_input"),
                        minLines = 3,
                        maxLines = 8,
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location / City") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    // Experience Slider
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Years of Experience", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text("${experienceYears.roundToInt()} Years", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ChampagneGold)
                        }
                        Slider(
                            value = experienceYears,
                            onValueChange = { experienceYears = it },
                            valueRange = 0f..20f,
                            colors = SliderDefaults.colors(thumbColor = ChampagneGold, activeTrackColor = ChampagneGold)
                        )
                    }

                    // Skills with "Add Custom Chip"
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Skills & Expertise (Tap to remove or add custom)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            selectedSkills.forEach { skill ->
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(AmberGold)
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            selectedSkills.remove(skill)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(skill, color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.Black, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = customSkillInput,
                                onValueChange = { customSkillInput = it },
                                placeholder = { Text("Add skill (e.g. Rust, PyTorch, Figma)", fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Button(
                                onClick = {
                                    val t = customSkillInput.trim()
                                    if (t.isNotEmpty() && !selectedSkills.contains(t)) {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        selectedSkills.add(t)
                                        customSkillInput = ""
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = Color.Black)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    }

                    // Looking For with "Add Custom Chip"
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Looking For (Tap to remove or add custom)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            selectedLookingFor.forEach { item ->
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(ChampagneGold)
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            selectedLookingFor.remove(item)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(item, color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.Black, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = customLookingForInput,
                                onValueChange = { customLookingForInput = it },
                                placeholder = { Text("Add looking for (e.g. Angel Investor, Design Lead)", fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Button(
                                onClick = {
                                    val t = customLookingForInput.trim()
                                    if (t.isNotEmpty() && !selectedLookingFor.contains(t)) {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        selectedLookingFor.add(t)
                                        customLookingForInput = ""
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = Color.Black)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    }

                    // Privacy Visibility Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Discover Visibility", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Show your profile deck to other builders in Discover feed", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = isVisible,
                            onCheckedChange = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                isVisible = it
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = ChampagneGold, checkedTrackColor = ChampagneGold.copy(alpha = 0.5f))
                        )
                    }

                    // Action Buttons (Save and Cancel)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                // Reset to current user values
                                name = currentUser?.name ?: ""
                                role = currentUser?.role ?: ""
                                headline = currentUser?.headline ?: ""
                                bio = currentUser?.bio ?: ""
                                location = currentUser?.location ?: "Bengaluru"
                                age = (currentUser?.age ?: 26).toFloat()
                                experienceYears = (currentUser?.experienceYears ?: 4).toFloat()
                                photoUrl = currentUser?.photoUrlsJson ?: ""
                                isVisible = currentUser?.isVisible ?: true
                                nameError = null
                                bioError = null
                                isEditing = false
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("cancel_edit_button"),
                            shape = CircleShape
                        ) {
                            Text("Discard", fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = {
                                validateAndSave()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("save_profile_button_bottom"),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ChampagneGold,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Changes", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        // Delete Confirmation Dialog
        if (showDeleteConfirmDialog) {
            Dialog(onDismissRequest = { showDeleteConfirmDialog = false }) {
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
                        Text("Delete Builder Account?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                        Text(
                            text = "This will permanently delete your swipes, match history, messages, and profile from The Brivia Club database.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showDeleteConfirmDialog = false },
                                modifier = Modifier.weight(1f),
                                shape = CircleShape
                            ) {
                                Text("Cancel")
                            }

                            Button(
                                onClick = {
                                    showDeleteConfirmDialog = false
                                    viewModel.deleteAccount {
                                        onNavigateOnboarding()
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }

            // Full Digital Membership Pass Modal Dialog
            if (showFullPassDialog && currentUser != null) {
                FullDigitalMembershipPassDialog(
                    user = currentUser!!,
                    planId = planId,
                    onDismiss = { showFullPassDialog = false }
                )
            }

            // Notification Center Bottom Sheet
            if (showNotificationCenter) {
                NotificationCenterSheet(
                    viewModel = viewModel,
                    onDismiss = { showNotificationCenter = false },
                    onNavigateToDestination = { dest ->
                        showNotificationCenter = false
                        if (dest == "membership") {
                            onNavigateUpgrade()
                        }
                    }
                )
            }
        }
    }
}

