package com.briviaclub.app.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.briviaclub.app.data.local.entity.PaymentEntity
import com.briviaclub.app.ui.components.BriviaTypographyLogo
import com.briviaclub.app.ui.components.DigitalMembershipCard
import com.briviaclub.app.ui.components.FullDigitalMembershipPassDialog
import com.briviaclub.app.ui.theme.AmberGold
import com.briviaclub.app.ui.theme.BronzeDark
import com.briviaclub.app.ui.theme.BronzeMetallic
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.EmeraldVerified
import com.briviaclub.app.ui.theme.SilverDark
import com.briviaclub.app.ui.theme.SilverMetallic
import com.briviaclub.app.ui.theme.SuperLikeBlue
import com.briviaclub.app.ui.viewmodel.BriviaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipScreen(
    viewModel: BriviaViewModel,
    onBack: (() -> Unit)? = null
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val subscription by viewModel.subscription.collectAsState()
    val payments by viewModel.payments.collectAsState()
    val haptic = LocalHapticFeedback.current

    var selectedTab by remember { mutableStateOf(0) } // 0: Plans & Upgrade, 1: Digital Pass & QR, 2: Invoices
    var selectedPlan by remember { mutableStateOf("pro") } // "pro" or "founder_vip"
    var couponInput by remember { mutableStateOf("") }
    var appliedCoupon by remember { mutableStateOf<String?>(null) }
    var discountPercent by remember { mutableStateOf(0) }

    var showPaymentModal by remember { mutableStateOf(false) }
    var showFullPassDialog by remember { mutableStateOf(false) }
    var selectedGateway by remember { mutableStateOf("Razorpay Instant UPI (GPay / PhonePe / Paytm)") }
    var lastSuccessfulPayment by remember { mutableStateOf<PaymentEntity?>(null) }

    val basePrice = when (selectedPlan) {
        "founder_vip" -> 2999.0
        "pro" -> 999.0
        else -> 0.0
    }
    val finalPrice = basePrice * (1.0 - (discountPercent / 100.0))

    val isDark = MaterialTheme.colorScheme.background.red < 0.2f

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BriviaTypographyLogo(isDark = isDark)

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (subscription?.planId == "free") DeepWine.copy(alpha = 0.12f)
                            else AmberGold.copy(alpha = 0.2f)
                        )
                        .border(
                            1.dp,
                            if (subscription?.planId == "free") DeepWine.copy(alpha = 0.3f) else AmberGold,
                            CircleShape
                        )
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = subscription?.planName ?: "Brivia Free",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        color = if (subscription?.planId == "free") DeepWine else (if (isDark) ChampagneGold else DeepWine)
                    )
                }
            }

            // Tabs (Plans vs Digital Pass vs Billing History)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = CommunityBadge,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = if (isDark) ChampagneGold else CommunityBadge,
                        height = 3.dp
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        selectedTab = 0
                    },
                    text = {
                        Text(
                            "VIP Plans",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        selectedTab = 1
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(14.dp))
                            Text(
                                "Digital Pass",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        selectedTab = 2
                    },
                    text = {
                        Text(
                            "Invoices (${payments.size})",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                )
            }

            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(250, easing = FastOutSlowInEasing)))
                        .togetherWith(fadeOut(animationSpec = tween(200)))
                },
                label = "MembershipTabTransition"
            ) { tab ->
                if (tab == 0) {
                    // Plans & Upgrade Tab
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Digital Pass Quick Access Banner
                        currentUser?.let { user ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        selectedTab = 1
                                    },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(ChampagneGold.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                Icons.Default.QrCode,
                                                contentDescription = null,
                                                tint = ChampagneGold,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = "Your Event Check-In Pass & QR",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "Tap to present QR code at club events",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    Text(
                                        text = "View Pass →",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ChampagneGold
                                    )
                                }
                            }
                        }
                        // Plan Option 1: Bronze Community
                        PlanCard(
                            tierEmoji = "🥉",
                            title = "Bronze Community Tier",
                            price = "₹0",
                            cadence = "/forever",
                            badge = "FOUNDATIONAL",
                            badgeColor = BronzeDark,
                            metallicAccent = BronzeMetallic,
                            isSelected = selectedPlan == "free",
                            isCurrentActive = subscription?.planId == "free",
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                selectedPlan = "free"
                            },
                            features = listOf(
                                "1 Super Like per day",
                                "Standard Discover feed visibility",
                                "1-on-1 direct messaging with matches",
                                "Custom skill & looking-for badges",
                                "Open community activity feed access"
                            )
                        )

                        // Plan Option 2: Silver Pro Builder
                        PlanCard(
                            tierEmoji = "🥈",
                            title = "Silver Pro Builder",
                            price = "₹999",
                            cadence = "/month",
                            badge = "MOST POPULAR",
                            badgeColor = SilverDark,
                            metallicAccent = SilverMetallic,
                            isSelected = selectedPlan == "pro",
                            isCurrentActive = subscription?.planId == "pro",
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                selectedPlan = "pro"
                            },
                            features = listOf(
                                "Unlimited daily swipes & matches",
                                "5 Super Likes per month",
                                "2x Profile feed visibility boost",
                                "Direct Pitch badge on chats",
                                "Filter by specific tech stacks"
                            )
                        )

                        // Plan Option 3: Gold Founder VIP
                        PlanCard(
                            tierEmoji = "🥇",
                            title = "Gold Founder VIP Tier",
                            price = "₹2,999",
                            cadence = "/month",
                            badge = "ALL-ACCESS ELITE",
                            badgeColor = DeepWine,
                            metallicAccent = ChampagneGold,
                            isSelected = selectedPlan == "founder_vip",
                            isCurrentActive = subscription?.planId == "founder_vip",
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                selectedPlan = "founder_vip"
                            },
                            features = listOf(
                                "Everything in Pro Builder",
                                "20 Super Likes per month",
                                "5x Priority profile boost in discover feed",
                                "Gold verified member insignia",
                                "Exclusive invite to Founder Demo Days & Investor mixers",
                                "Direct export of match contacts"
                            )
                        )

                        // Coupon Promo Code Box
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Have a Community Promo Code?",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = couponInput,
                                        onValueChange = { couponInput = it.uppercase() },
                                        placeholder = { Text("e.g. SHIP20 or BUILD50", fontSize = 12.sp) },
                                        leadingIcon = { Icon(Icons.Default.LocalOffer, contentDescription = null, modifier = Modifier.size(18.dp)) },
                                        singleLine = true,
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp)
                                    )

                                    Button(
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            if (couponInput == "BUILD50") {
                                                appliedCoupon = "BUILD50"
                                                discountPercent = 50
                                            } else if (couponInput == "SHIP20") {
                                                appliedCoupon = "SHIP20"
                                                discountPercent = 20
                                            } else if (couponInput.isNotEmpty()) {
                                                appliedCoupon = couponInput
                                                discountPercent = 15
                                            }
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = CommunityBadge)
                                    ) {
                                        Text("Apply")
                                    }
                                }

                                if (appliedCoupon != null) {
                                    Text(
                                        text = "🎉 Code $appliedCoupon applied! $discountPercent% discount activated.",
                                        color = EmeraldVerified,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Checkout summary action
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = DeepWine)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = when (selectedPlan) {
                                            "founder_vip" -> "Founder VIP Pass"
                                            "pro" -> "Pro Builder Pass"
                                            else -> "Bronze Community Pass"
                                        },
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = if (finalPrice > 0) "₹${finalPrice.toInt()}/mo" else "Free Forever",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ChampagneGold
                                    )
                                }

                                if (selectedPlan == "free") {
                                    if (subscription?.planId == "free") {
                                        OutlinedButton(
                                            onClick = { },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(50.dp),
                                            shape = CircleShape,
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                                        ) {
                                            Text("✓ Current Active Free Tier", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        Button(
                                            onClick = {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                viewModel.upgradePlan(
                                                    planId = "free",
                                                    planName = "Bronze Community",
                                                    amount = 0.0,
                                                    gateway = "Community Free Downgrade",
                                                    couponCode = ""
                                                ) { payment ->
                                                    lastSuccessfulPayment = payment
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(50.dp),
                                            shape = CircleShape,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = BronzeMetallic,
                                                contentColor = Color.White
                                            )
                                        ) {
                                            Text("Switch to Bronze Free Tier", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            showPaymentModal = true
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp),
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = ChampagneGold,
                                            contentColor = Color.Black
                                        )
                                    ) {
                                        Text("Proceed to Checkout", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                } else if (tab == 1) {
                    // Digital Pass & QR Tab
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Your Official Digital Membership Pass",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Present this unique scannable QR code at private founder dinners, hackathons, and demo day stages for instant admittance.",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        currentUser?.let { user ->
                            DigitalMembershipCard(
                                user = user,
                                planId = subscription?.planId ?: "free",
                                onExpandClick = { showFullPassDialog = true }
                            )
                        }

                        // Event Access Perks Info Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WorkspacePremium,
                                        contentDescription = null,
                                        tint = ChampagneGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Club Event Door Policy",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = "• Door scanners dynamically verify active membership credentials and photo ID.\n• Gold Founder VIP members receive backstage access & speaker lounge entry.\n• Pro Builder members receive priority fast-track entry.\n• Passes are non-transferable and cryptographically linked to your builder ID.",
                                    fontSize = 11.sp,
                                    lineHeight = 17.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                } else {
                    // Invoices & Billing History Tab
                    if (payments.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Receipt,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(44.dp)
                                )
                                Text(
                                    text = "No past invoices",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "When you upgrade, your tax invoices and receipts will appear here.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(payments) { p ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn(tween(300)) + slideInVertically(
                                        initialOffsetY = { 30 },
                                        animationSpec = tween(300)
                                    )
                                ) {
                                    PaymentReceiptItem(p)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Simulated Checkout Modal
        if (showPaymentModal) {
            Dialog(onDismissRequest = { showPaymentModal = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Payment Gateway",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(EmeraldVerified.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("SECURE 256-BIT UPI", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldVerified)
                            }
                        }

                        Text(
                            text = "Upgrading to ${if (selectedPlan == "founder_vip") "Founder VIP Pass" else "Pro Builder Pass"}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Total Payable: ₹${finalPrice.toInt()} INR",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ChampagneGold
                        )

                        Text("Select Indian Payment Rails", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

                        val gateways = listOf(
                            "Razorpay Instant UPI (GPay / PhonePe / Paytm)",
                            "CRED Pay & NetBanking",
                            "Cards (RuPay / Visa / Mastercard)"
                        )
                        gateways.forEach { gw ->
                            val isChosen = selectedGateway == gw
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isChosen) AmberGold.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        selectedGateway = gw
                                    }
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = gw,
                                    fontSize = 12.sp,
                                    fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isChosen) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AmberGold, modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.upgradePlan(
                                    planId = selectedPlan,
                                    planName = if (selectedPlan == "founder_vip") "Founder VIP" else "Pro Builder",
                                    amount = finalPrice,
                                    gateway = selectedGateway,
                                    couponCode = appliedCoupon ?: ""
                                ) { payment ->
                                    lastSuccessfulPayment = payment
                                    showPaymentModal = false
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold, contentColor = Color.Black)
                        ) {
                            Text("Pay ₹${finalPrice.toInt()}", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                showPaymentModal = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = CircleShape
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }

        // Payment Success Celebration Dialog
        lastSuccessfulPayment?.let { payment ->
            Dialog(onDismissRequest = { lastSuccessfulPayment = null }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(EmeraldVerified.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldVerified,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Text(
                            text = "Membership Activated!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "Invoice #${payment.invoiceNumber} generated. Your unlimited swipes, boosts, and VIP perks are now active on your profile.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Button(
                            onClick = { lastSuccessfulPayment = null },
                            modifier = Modifier.fillMaxWidth(),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = CommunityBadge)
                        ) {
                            Text("Enjoy The Club", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Full Digital Membership Pass Dialog
        if (showFullPassDialog && currentUser != null) {
            FullDigitalMembershipPassDialog(
                user = currentUser!!,
                planId = subscription?.planId ?: "free",
                onDismiss = { showFullPassDialog = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlanCard(
    tierEmoji: String,
    title: String,
    price: String,
    cadence: String,
    badge: String,
    badgeColor: Color,
    metallicAccent: Color,
    isSelected: Boolean,
    isCurrentActive: Boolean = false,
    onClick: () -> Unit,
    features: List<String>
) {
    val isDark = MaterialTheme.colorScheme.background.red < 0.2f

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF131722) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            if (isSelected) metallicAccent else MaterialTheme.colorScheme.outline
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 6.dp else 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(metallicAccent.copy(alpha = 0.2f))
                            .border(1.2.dp, metallicAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = tierEmoji, fontSize = 20.sp)
                    }

                    Column {
                        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(text = price, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = metallicAccent)
                            Text(text = cadence, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(badgeColor)
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Text(text = badge, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    if (isCurrentActive) {
                        Text(
                            text = "✓ ACTIVE",
                            color = EmeraldVerified,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

            features.forEach { feat ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = EmeraldVerified,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(text = feat, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun PaymentReceiptItem(payment: PaymentEntity) {
    val formatter = remember { SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault()) }
    val dateStr = remember(payment.timestamp) { formatter.format(Date(payment.timestamp)) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "${payment.planName} Pass",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Inv #${payment.invoiceNumber} • ${payment.gateway}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = dateStr,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "₹${payment.amount.toInt()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AmberGold
                )
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(EmeraldVerified.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "PAID",
                        color = EmeraldVerified,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
