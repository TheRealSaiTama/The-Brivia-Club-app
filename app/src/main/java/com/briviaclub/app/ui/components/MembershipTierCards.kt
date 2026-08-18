package com.briviaclub.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.briviaclub.app.ui.theme.AmberGold
import com.briviaclub.app.ui.theme.BronzeDark
import com.briviaclub.app.ui.theme.BronzeLight
import com.briviaclub.app.ui.theme.BronzeMetallic
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.EmeraldVerified
import com.briviaclub.app.ui.theme.SilverDark
import com.briviaclub.app.ui.theme.SilverLight
import com.briviaclub.app.ui.theme.SilverMetallic

enum class MembershipTier(
    val id: String,
    val title: String,
    val subtitle: String,
    val priceText: String,
    val badgeEmoji: String,
    val badgeLabel: String,
    val gradientColors: List<Color>,
    val accentColor: Color,
    val perks: List<String>,
    val isPopular: Boolean = false
) {
    BRONZE(
        id = "free",
        title = "Bronze Tier",
        subtitle = "Foundational Community Member",
        priceText = "Free Lifetime",
        badgeEmoji = "🥉",
        badgeLabel = "BRONZE",
        gradientColors = listOf(Color(0xFF8C531B), Color(0xFFB87333), Color(0xFFCD7F32), Color(0xFFE5A65D)),
        accentColor = BronzeMetallic,
        perks = listOf(
            "1 Super Like per day",
            "Standard Discover feed visibility",
            "1-on-1 direct messaging with matches",
            "Custom skill & looking-for badges",
            "Access to open community activity feed"
        )
    ),
    SILVER(
        id = "pro",
        title = "Silver Tier",
        subtitle = "Pro Builder & Accelerated Match",
        priceText = "₹999 / mo",
        badgeEmoji = "🥈",
        badgeLabel = "SILVER PRO",
        gradientColors = listOf(Color(0xFF475569), Color(0xFF64748B), Color(0xFF94A3B8), Color(0xFFCBD5E1)),
        accentColor = SilverMetallic,
        perks = listOf(
            "Unlimited daily swipes & matches",
            "5 Super Likes per month",
            "2x Profile feed visibility boost",
            "Stack & role advanced discovery filters",
            "Silver verified profile crest",
            "Read receipts on direct chats"
        ),
        isPopular = true
    ),
    GOLD(
        id = "founder_vip",
        title = "Gold VIP Tier",
        subtitle = "Elite Founder & Investor Access",
        priceText = "₹2,999 / mo",
        badgeEmoji = "🥇",
        badgeLabel = "GOLD ELITE",
        gradientColors = listOf(Color(0xFF92400E), Color(0xFFB45309), Color(0xFFD97706), Color(0xFFF59E0B), Color(0xFFFDE68A)),
        accentColor = AmberGold,
        perks = listOf(
            "5x Priority Discover algorithm placement",
            "20 Super Likes per month",
            "Gold metallic verified member insignia",
            "Direct Pitch Deck inbox to angel investors",
            "Invites to Private Founder Demo Days & Mixers",
            "Direct CSV/Contact export of all connections"
        )
    )
}

@Composable
fun MembershipTierCardsSection(
    currentPlanId: String,
    onSelectTier: (MembershipTier) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Membership Tiers & Privileges",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Visual breakdown of Gold, Silver, and Bronze privileges",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        MembershipTier.values().forEach { tier ->
            val isCurrent = (currentPlanId == tier.id) || (currentPlanId == "free" && tier == MembershipTier.BRONZE)
            MembershipTierCard(
                tier = tier,
                isCurrentTier = isCurrent,
                onCardClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSelectTier(tier)
                }
            )
        }
    }
}

@Composable
fun MembershipTierCard(
    tier: MembershipTier,
    isCurrentTier: Boolean,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val isDark = MaterialTheme.colorScheme.background.red < 0.2f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onCardClick() }
            .animateContentSize()
            .testTag("tier_card_${tier.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF131722) else Color(0xFFFFFFFF)
        ),
        border = BorderStroke(
            width = if (isCurrentTier) 2.dp else 1.2.dp,
            brush = if (isCurrentTier) Brush.horizontalGradient(tier.gradientColors)
            else Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.outline, MaterialTheme.colorScheme.outline))
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrentTier) 6.dp else 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Metallic Accent Top Strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(Brush.horizontalGradient(tier.gradientColors))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header Row: Emoji badge, Tier Name, Pricing, and Current Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Metallic Crest Avatar
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(tier.gradientColors))
                                .border(1.5.dp, Color.White.copy(alpha = 0.6f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tier.badgeEmoji,
                                fontSize = 22.sp
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = tier.title,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (tier.isPopular) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(DeepWine)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "POPULAR",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            Text(
                                text = tier.subtitle,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Price Pill
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = tier.priceText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = tier.accentColor
                        )
                        if (isCurrentTier) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 3.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(EmeraldVerified.copy(alpha = 0.15f))
                                    .border(1.dp, EmeraldVerified, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "CURRENT PLAN",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldVerified
                                )
                            }
                        }
                    }
                }

                // Perks Preview (2 Key Perks)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    val displayPerks = if (expanded) tier.perks else tier.perks.take(2)
                    displayPerks.forEach { perk ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(tier.accentColor.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = tier.accentColor,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                            Text(
                                text = perk,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Expand / Collapse Perks & Action Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                expanded = !expanded
                            }
                            .padding(vertical = 4.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (expanded) "Show Less" else "View All ${tier.perks.size} Privileges",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = tier.accentColor
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = tier.accentColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    if (isCurrentTier) {
                        Text(
                            text = "✓ Active Status",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldVerified
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Brush.horizontalGradient(tier.gradientColors))
                                .clickable { onCardClick() }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (tier == MembershipTier.BRONZE) "Switch to Bronze" else "Select ${tier.badgeLabel}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
