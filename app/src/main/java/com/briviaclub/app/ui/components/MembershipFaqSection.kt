package com.briviaclub.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.briviaclub.app.ui.theme.AmberGold
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.EmeraldVerified

data class FaqItem(
    val id: String,
    val question: String,
    val answer: String,
    val category: String = "Membership"
)

val defaultMembershipFaqs = listOf(
    FaqItem(
        id = "faq_tiers",
        question = "How do membership tiers and event admittance work?",
        answer = "The Brivia Club offers Community (Bronze), Pro Builder (Silver), and Founder VIP (Gold) tiers. Community members have access to public hackathons and open demo days. Pro Builders receive invitations to monthly private mixers and verified matchmaking. Founder VIPs enjoy unlimited admittance to private investor dinners, VIP club lounges, and priority demo stage presentations. Show your Digital Pass QR code at the door for instant check-in.",
        category = "Tiers & Access"
    ),
    FaqItem(
        id = "faq_digital_pass",
        question = "How does the Digital Pass & QR check-in work?",
        answer = "Every registered member receives a unique, cryptographically signed Member ID (e.g., BC-GLD-7821-K91A) and live QR code. You can present your pass directly from the Profile or Membership screen. Door staff scan your code using our verification scanner to grant instantaneous admittance.",
        category = "Digital Pass"
    ),
    FaqItem(
        id = "faq_billing",
        question = "Can I upgrade, switch plans, or cancel anytime?",
        answer = "Yes! Upgrades take effect immediately with full tier privileges unlocked on the spot. You can switch between Monthly and Annual billing or cancel anytime with zero lock-in contracts or penalty fees. Your perks remain active through the end of your paid billing cycle.",
        category = "Billing"
    ),
    FaqItem(
        id = "faq_matching",
        question = "How do member discovery and direct chats work?",
        answer = "Browse verified founders in Discover mode and swipe right or tap Super Like to express interest. When two members mutually connect, a private encrypted chat channel is opened in Club Messages.",
        category = "Networking"
    ),
    FaqItem(
        id = "faq_notifications",
        question = "How do I customize push alerts and feed notifications?",
        answer = "You can manage granular push notification settings in the Settings panel or Notification Center. Choose whether to receive real-time alerts for member feed milestones, collaboration posts, or membership tier status changes.",
        category = "Settings"
    ),
    FaqItem(
        id = "faq_concierge",
        question = "How do I contact the Club Concierge for private assistance?",
        answer = "Founder VIP and Pro members have 24/7 dedicated support via our in-app Concierge desk. You can initiate a private concierge inquiry directly from the help button below or inside your VIP Membership tab.",
        category = "Support"
    )
)

/**
 * Accordion-style FAQ component for Membership and Settings screens
 */
@Composable
fun MembershipFaqSection(
    modifier: Modifier = Modifier,
    isDark: Boolean = MaterialTheme.colorScheme.background.red < 0.2f,
    onContactConcierge: (() -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current
    var expandedItemId by remember { mutableStateOf<String?>(defaultMembershipFaqs.firstOrNull()?.id) }
    var searchQuery by remember { mutableStateOf("") }
    var showConciergeSentNotice by remember { mutableStateOf(false) }

    val filteredFaqs = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            defaultMembershipFaqs
        } else {
            val q = searchQuery.trim().lowercase()
            defaultMembershipFaqs.filter {
                it.question.lowercase().contains(q) ||
                    it.answer.lowercase().contains(q) ||
                    it.category.lowercase().contains(q)
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("membership_faq_section"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
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
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (isDark) DeepWine.copy(alpha = 0.4f) else ChampagneGold.copy(alpha = 0.2f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = "FAQ",
                            tint = if (isDark) ChampagneGold else DeepWine,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Membership FAQ & Help",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Instant answers to common membership questions",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CommunityBadge.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${filteredFaqs.size} TOPICS",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp,
                        color = CommunityBadge
                    )
                }
            }

            // Search Bar for FAQs
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        "Search questions (e.g. tiers, pass, billing)...",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search FAQs",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = if (isDark) Color(0xFF141720) else Color(0xFFF9FAFB),
                    unfocusedContainerColor = if (isDark) Color(0xFF141720) else Color(0xFFF9FAFB),
                    focusedBorderColor = CommunityBadge,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("faq_search_input")
            )

            // Accordion Items List
            if (filteredFaqs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "No matching questions found",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Try searching for 'tiers', 'QR pass', or 'billing'",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filteredFaqs.forEachIndexed { index, faq ->
                        val isExpanded = expandedItemId == faq.id

                        AccordionFaqCard(
                            faq = faq,
                            isExpanded = isExpanded,
                            isDark = isDark,
                            index = index,
                            onToggle = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                expandedItemId = if (isExpanded) null else faq.id
                            }
                        )
                    }
                }
            }

            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            // Need More Help Footer / Concierge Action
            if (showConciergeSentNotice) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(EmeraldVerified.copy(alpha = 0.15f))
                        .border(1.dp, EmeraldVerified.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = EmeraldVerified,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Inquiry sent! The Brivia Concierge team will reach out via Club Chat.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else {
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
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = null,
                            tint = AmberGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "Still have questions?",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Speak with Brivia Club Concierge",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            if (onContactConcierge != null) {
                                onContactConcierge()
                            } else {
                                showConciergeSentNotice = true
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) DeepWine else Color(0xFF2C1810),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("contact_concierge_button")
                    ) {
                        Text(
                            text = "Ask Concierge",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Individual Expandable Accordion Item
 */
@Composable
fun AccordionFaqCard(
    faq: FaqItem,
    isExpanded: Boolean,
    isDark: Boolean,
    index: Int,
    onToggle: () -> Unit
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "arrowRotation"
    )

    val cardBg = if (isExpanded) {
        if (isDark) Color(0xFF1E2230) else Color(0xFFF6F8FC)
    } else {
        if (isDark) Color(0xFF141722) else Color(0xFFFAFBFD)
    }

    val borderColor = if (isExpanded) {
        if (isDark) ChampagneGold.copy(alpha = 0.5f) else CommunityBadge.copy(alpha = 0.5f)
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onToggle() }
            .testTag("faq_card_$index"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // Header Row (Question + Category Pill + Chevron)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(
                                if (isExpanded) ChampagneGold else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            )
                    )

                    Text(
                        text = faq.question,
                        fontSize = 13.sp,
                        fontWeight = if (isExpanded) FontWeight.Bold else FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            if (isExpanded) CommunityBadge.copy(alpha = 0.15f) else Color.Transparent
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse answer" else "Expand answer",
                        tint = if (isExpanded) CommunityBadge else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(rotationAngle)
                            .testTag("faq_chevron_$index")
                    )
                }
            }

            // Expandable Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(
                    animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(durationMillis = 250)),
                exit = shrinkVertically(
                    animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(durationMillis = 180))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 14.dp, end = 6.dp)
                ) {
                    Divider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = faq.answer,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Category Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (isDark) Color(0xFF2A3042) else Color(0xFFE8EEF8)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "🏷️ ${faq.category}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) ChampagneGold else DeepWine
                        )
                    }
                }
            }
        }
    }
}
