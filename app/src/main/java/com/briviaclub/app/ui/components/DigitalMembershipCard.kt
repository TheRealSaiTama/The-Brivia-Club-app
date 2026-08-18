package com.briviaclub.app.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.briviaclub.app.data.local.entity.UserEntity
import com.briviaclub.app.ui.theme.AmberGold
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.EmeraldVerified
import com.briviaclub.app.util.QrCodeGenerator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Luxury Digital Membership Card with Dynamic Scannable QR Code and Event Check-In Features.
 */
@Composable
fun DigitalMembershipCard(
    user: UserEntity,
    planId: String,
    modifier: Modifier = Modifier,
    onExpandClick: (() -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    var showCheckInDialog by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf("pass") } // "pass" or "privileges"

    val memberId = remember(user.id, planId) {
        QrCodeGenerator.formatMemberId(user.id, planId)
    }

    val checkInPayload = remember(user.id, user.name, planId, user.location) {
        QrCodeGenerator.buildCheckInPayload(user.id, user.name, planId, user.location)
    }

    val qrBitmap = remember(checkInPayload) {
        QrCodeGenerator.generateQrBitmap(
            content = checkInPayload,
            size = 400,
            darkColor = android.graphics.Color.BLACK,
            lightColor = android.graphics.Color.WHITE
        )
    }

    val tierConfig = remember(planId) {
        when (planId) {
            "founder_vip" -> TierPassConfig(
                name = "FOUNDER VIP",
                badge = "✨ GOLD TIER",
                gradient = listOf(Color(0xFFD4AF37), Color(0xFFFFDF73), Color(0xFFAA771C)),
                background = Color(0xFF14120B),
                accentColor = ChampagneGold,
                eventAccess = "All Private Dinners & Demo Day Stage"
            )
            "pro" -> TierPassConfig(
                name = "PRO BUILDER",
                badge = "🥈 SILVER TIER",
                gradient = listOf(Color(0xFFC0C0C0), Color(0xFFE8E8E8), Color(0xFF8A8A8A)),
                background = Color(0xFF10141B),
                accentColor = Color(0xFFE2E8F0),
                eventAccess = "Monthly Mixers & Hackathons"
            )
            else -> TierPassConfig(
                name = "COMMUNITY MEMBER",
                badge = "🥉 BRONZE TIER",
                gradient = listOf(Color(0xFFCD7F32), Color(0xFFE5A869), Color(0xFF8C4C14)),
                background = Color(0xFF171210),
                accentColor = Color(0xFFE5A869),
                eventAccess = "Open Demo Days & Community Mixers"
            )
        }
    }

    // Shimmer transition for luxury card edge
    val infiniteTransition = rememberInfiniteTransition(label = "card_shimmer")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .shadow(12.dp, RoundedCornerShape(26.dp))
            .testTag("digital_membership_card"),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = tierConfig.background),
        border = BorderStroke(1.5.dp, Brush.linearGradient(tierConfig.gradient))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card Header: Club Crest + Tier Badge
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
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(tierConfig.gradient)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "B",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Serif,
                            fontSize = 17.sp
                        )
                    }
                    Column {
                        Text(
                            text = "THE BRIVIA CLUB",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 2.sp,
                            color = tierConfig.accentColor
                        )
                        Text(
                            text = "OFFICIAL DIGITAL PASS",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                // Tier Pill
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(tierConfig.accentColor.copy(alpha = 0.25f))
                        .border(1.dp, Brush.linearGradient(tierConfig.gradient), CircleShape)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = tierConfig.badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.6.sp,
                        color = Color.White
                    )
                }
            }

            // Member Info & Photo Summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Member Avatar
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, Brush.linearGradient(tierConfig.gradient), CircleShape)
                        .padding(2.dp)
                ) {
                    if (user.photoUrlsJson.isNotBlank()) {
                        AsyncImage(
                            model = user.photoUrlsJson,
                            contentDescription = user.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color(0xFF2A2D3C)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user.name.take(1).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = user.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        if (user.isVerified) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified Member",
                                tint = EmeraldVerified,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Text(
                        text = user.role,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = tierConfig.accentColor.copy(alpha = 0.9f)
                    )

                    Text(
                        text = "Chapter: ${user.location}",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }

            Divider(color = Color.White.copy(alpha = 0.12f), thickness = 1.dp)

            // Scannable QR Code Display Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF0B0D13))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(18.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // QR Code White Frame Container
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (qrBitmap != null) {
                            Image(
                                bitmap = qrBitmap.asImageBitmap(),
                                contentDescription = "Unique Membership QR Code for Event Check-In",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .testTag("membership_qr_code_image")
                            )
                        } else {
                            Text(
                                "Generating QR...",
                                color = Color.Black,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Member ID Display & Copy Button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.08f))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Membership ID", memberId)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied Member ID: $memberId", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("copy_member_id_pill")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = tierConfig.accentColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "MEMBER ID: $memberId",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Member ID",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    // Security & Access Watermark
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(EmeraldVerified)
                        )
                        Text(
                            text = "SCAN AT CLUB ENTRANCE • ACTIVE PASS",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = EmeraldVerified
                        )
                    }
                }
            }

            // Bottom Action Controls: Simulate Event Check-in & Expand
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        showCheckInDialog = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("simulate_event_checkin_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tierConfig.accentColor,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Simulate Check-In",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                if (onExpandClick != null) {
                    OutlinedButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onExpandClick()
                        },
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("expand_digital_pass_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Full Pass",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // Event Check-in Simulation Dialog
    if (showCheckInDialog) {
        EventCheckInSimulationDialog(
            user = user,
            memberId = memberId,
            tierConfig = tierConfig,
            onDismiss = { showCheckInDialog = false }
        )
    }
}

/**
 * Event Check-In Scanner Simulation Dialog
 */
@Composable
fun EventCheckInSimulationDialog(
    user: UserEntity,
    memberId: String,
    tierConfig: TierPassConfig,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var isVerifying by remember { mutableStateOf(true) }
    var isVerifiedSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Simulate real-time club gate scanner verification
        kotlinx.coroutines.delay(1200)
        isVerifying = false
        isVerifiedSuccess = true
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    val timestamp = remember {
        SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault()).format(Date())
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(28.dp))
                .testTag("event_checkin_dialog"),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141722)),
            border = BorderStroke(1.5.dp, Brush.linearGradient(tierConfig.gradient))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EVENT CHECK-IN SCANNER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp,
                        color = tierConfig.accentColor
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }

                if (isVerifying) {
                    // Scanning State
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.padding(vertical = 24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(tierConfig.accentColor.copy(alpha = 0.15f))
                                .border(2.dp, tierConfig.accentColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null,
                                tint = tierConfig.accentColor,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Text(
                            text = "Scanning Digital Pass...",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Validating cryptographic signature with Brivia Gate Server",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    // Success State
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(EmeraldVerified.copy(alpha = 0.15f))
                                .border(2.dp, EmeraldVerified, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Check-in successful",
                                tint = EmeraldVerified,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Text(
                            text = "ADMITTANCE GRANTED",
                            color = EmeraldVerified,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp
                        )

                        Text(
                            text = "Welcome to the Club, ${user.name}",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        // Event Summary Card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF1E2230))
                                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Event:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
                                    Text("Brivia Founder Mixer & Demo Day", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Member ID:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
                                    Text(memberId, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = tierConfig.accentColor)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Access Tier:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
                                    Text(tierConfig.badge, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Privileges:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
                                    Text(tierConfig.eventAccess, fontSize = 11.sp, color = tierConfig.accentColor)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Timestamp:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.5f))
                                    Text(timestamp, fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                                }
                            }
                        }

                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("done_checkin_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldVerified, contentColor = Color.White)
                        ) {
                            Text("Done • Enter Event", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Full Screen / High-Brightness Digital Membership Pass Modal
 */
@Composable
fun FullDigitalMembershipPassDialog(
    user: UserEntity,
    planId: String,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    val memberId = remember(user.id, planId) {
        QrCodeGenerator.formatMemberId(user.id, planId)
    }

    val checkInPayload = remember(user.id, user.name, planId, user.location) {
        QrCodeGenerator.buildCheckInPayload(user.id, user.name, planId, user.location)
    }

    val qrBitmap = remember(checkInPayload) {
        QrCodeGenerator.generateQrBitmap(
            content = checkInPayload,
            size = 512,
            darkColor = android.graphics.Color.BLACK,
            lightColor = android.graphics.Color.WHITE
        )
    }

    val tierConfig = remember(planId) {
        when (planId) {
            "founder_vip" -> TierPassConfig(
                name = "FOUNDER VIP",
                badge = "✨ GOLD TIER",
                gradient = listOf(Color(0xFFD4AF37), Color(0xFFFFDF73), Color(0xFFAA771C)),
                background = Color(0xFF14120B),
                accentColor = ChampagneGold,
                eventAccess = "All Private Dinners & Demo Day Stage"
            )
            "pro" -> TierPassConfig(
                name = "PRO BUILDER",
                badge = "🥈 SILVER TIER",
                gradient = listOf(Color(0xFFC0C0C0), Color(0xFFE8E8E8), Color(0xFF8A8A8A)),
                background = Color(0xFF10141B),
                accentColor = Color(0xFFE2E8F0),
                eventAccess = "Monthly Mixers & Hackathons"
            )
            else -> TierPassConfig(
                name = "COMMUNITY MEMBER",
                badge = "🥉 BRONZE TIER",
                gradient = listOf(Color(0xFFCD7F32), Color(0xFFE5A869), Color(0xFF8C4C14)),
                background = Color(0xFF171210),
                accentColor = Color(0xFFE5A869),
                eventAccess = "Open Demo Days & Community Mixers"
            )
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.92f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Top close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CLUB CHECK-IN PASS",
                        color = ChampagneGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                // Digital Card Container
                DigitalMembershipCard(
                    user = user,
                    planId = planId,
                    modifier = Modifier.fillMaxWidth()
                )

                // High-Brightness Check-in Hint
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1B1F2C))
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = ChampagneGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "Present this QR code to door staff",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "QR code contains your encrypted membership token and admits you to ${tierConfig.eventAccess}.",
                                color = Color.White.copy(alpha = 0.65f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

data class TierPassConfig(
    val name: String,
    val badge: String,
    val gradient: List<Color>,
    val background: Color,
    val accentColor: Color,
    val eventAccess: String
)
