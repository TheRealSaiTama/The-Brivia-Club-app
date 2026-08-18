package com.briviaclub.app.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Swipe
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.briviaclub.app.ui.components.BriviaTypographyLogo
import com.briviaclub.app.ui.theme.AmberGold
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.EmeraldVerified
import com.briviaclub.app.ui.theme.SuperLikeBlue
import com.briviaclub.app.ui.viewmodel.BriviaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: BriviaViewModel,
    onBack: (() -> Unit)? = null
) {
    val stats by viewModel.stats.collectAsState()
    val allProfiles by viewModel.rawProfiles.collectAsState()
    val matches by viewModel.matches.collectAsState()
    val reports by viewModel.reports.collectAsState()
    val subscription by viewModel.subscription.collectAsState()

    val totalUsers = allProfiles.size + 1 // including current user
    val verifiedBuilders = allProfiles.count { it.isVerified } + 1
    val verifiedRate = if (totalUsers > 0) (verifiedBuilders * 100 / totalUsers) else 100

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            val isDark = MaterialTheme.colorScheme.background.red < 0.2f

            // Screen Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BriviaTypographyLogo(isDark = isDark)

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(EmeraldVerified.copy(alpha = 0.15f))
                        .border(1.dp, EmeraldVerified.copy(alpha = 0.35f), CircleShape)
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
                            text = "LIVE METRICS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldVerified
                        )
                    }
                }
            }

            // Top Hero Card: Brivia Ecosystem Overview
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = DeepWine)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NETWORK ACTIVITY",
                            color = ChampagneGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(EmeraldVerified.copy(alpha = 0.25f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
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
                                Text("LIVE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem(title = "Total Builders", value = "$totalUsers", icon = Icons.Default.People)
                        StatItem(title = "Total Swipes", value = "${stats.totalSwipes}", icon = Icons.Default.Swipe)
                        StatItem(title = "Active Teams", value = "${matches.size}", icon = Icons.Default.Handshake)
                        StatItem(title = "Match Rate", value = "${stats.matchRate}%", icon = Icons.Default.TrendingUp)
                    }
                }
            }

            // Skill Distribution in Club
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Builder Skill Ecosystem",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    SkillBar("AI / ML & Agents", 0.42f, AmberGold)
                    SkillBar("Fullstack & Mobile Dev", 0.78f, CommunityBadge)
                    SkillBar("Product & UI/UX Design", 0.55f, SuperLikeBlue)
                    SkillBar("Growth, GTM & Marketing", 0.38f, EmeraldVerified)
                    SkillBar("Solana / Web3 / Infra", 0.25f, ChampagneGold)
                }
            }

            // Trust & Safety Moderation Stats
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Trust, Safety & Moderation",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = EmeraldVerified,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Verified Rate", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("$verifiedRate%", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = EmeraldVerified)
                        }
                        Column {
                            Text("Community Reports", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${reports.size}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Column {
                            Text("Auto-Mod Response", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("< 2 mins", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SuperLikeBlue)
                        }
                    }

                    if (reports.isNotEmpty()) {
                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                        Text(
                            text = "Recent Moderation Actions",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        reports.take(3).forEach { rep ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Blocked/Reported: ${rep.reason}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.12f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("RESOLVED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }

            // High Engagement Tech Stacks
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
                    Text(
                        text = "Most In-Demand Collaborations",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    CollaborationDemandRow("AI SaaS + Next.js / Kotlin", "🔥 Very High", AmberGold)
                    CollaborationDemandRow("Fintech / Crypto + Rust", "⚡ High", SuperLikeBlue)
                    CollaborationDemandRow("YC / Hackathon Teammates", "🚀 Surging", EmeraldVerified)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatItem(title: String, value: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null, tint = ChampagneGold, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text(text = title, fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
    }
}

@Composable
private fun SkillBar(skill: String, percentage: Float, barColor: Color) {
    val animatedProgress by androidx.compose.animation.core.animateFloatAsState(
        targetValue = percentage,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 800, easing = androidx.compose.animation.core.FastOutSlowInEasing),
        label = "skill_progress"
    )

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(skill, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text("${(percentage * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = barColor)
        }
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = barColor,
            trackColor = barColor.copy(alpha = 0.15f)
        )
    }
}

@Composable
private fun CollaborationDemandRow(title: String, status: String, statusColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        Text(status, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = statusColor)
    }
}
