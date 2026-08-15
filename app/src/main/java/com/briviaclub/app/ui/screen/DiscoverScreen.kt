package com.briviaclub.app.ui.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.briviaclub.app.ui.theme.BriviaClubAppTheme
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.PrimaryBackground
import com.briviaclub.app.ui.theme.PrimaryText
import com.briviaclub.app.ui.theme.SecondaryText
import kotlinx.coroutines.delay

private val TagBgLight = Color(0xFFF3ECEE)
private val CardBorderColor = Color(0xFFEFE6E8)

private data class PartnerProfile(
    val initial: String,
    val name: String,
    val age: String,
    val role: String,
    val location: String,
    val tagline: String,
    val matchPercent: String,
    val verified: String,
    val bio: String,
    val interests: List<String>
)

private val sampleDeck = listOf(
    PartnerProfile(
        initial = "A",
        name = "Ananya Rao",
        age = "24",
        role = "Product Designer • NID",
        location = "Bengaluru • 5 km away",
        tagline = "Hackathon mentor • AI UX • Fintech",
        matchPercent = "94%",
        verified = "Verified Builder",
        bio = "Building AI-driven design systems. Looking for a fullstack co-founder to ship a scalable MVP for B2B SaaS applications.",
        interests = listOf("🎨 Figma", "⚡ React", "🤖 LLMs", "📍 Bengaluru", "💼 Co-founder", "🏆 Hackathons")
    ),
    PartnerProfile(
        initial = "D",
        name = "Dev Patel",
        age = "26",
        role = "Full-stack engineer • IIT Bombay",
        location = "Pune • 8 km away",
        tagline = "Node.js • Startup ops • Growth",
        matchPercent = "92%",
        verified = "Verified Builder",
        bio = "Shipped 3 MVPs in 18 months. Looking to pair with a sharp product/design mind for the next AI-native tool.",
        interests = listOf("⚛️ React", "🟢 Node", "☁️ AWS", "📍 Pune", "💼 Co-founder", "🚀 Startups")
    ),
    PartnerProfile(
        initial = "R",
        name = "Rhea Sen",
        age = "29",
        role = "Product Lead • Mumbai",
        location = "Mumbai • 12 km away",
        tagline = "Design systems • Brand building • Growth",
        matchPercent = "91%",
        verified = "Verified Builder",
        bio = "Ex-fintech PM leading product discovery. Seeking a technical co-founder for a vertical SaaS experiment.",
        interests = listOf("📊 Metrics", "🧠 Strategy", "💼 SaaS", "📍 Mumbai", "💼 Co-founder", "🏆 Hackathons")
    ),
    PartnerProfile(
        initial = "K",
        name = "Kabir Mehta",
        age = "34",
        role = "Startup advisor • Bengaluru",
        location = "Bengaluru • 2 km away",
        tagline = "Scale-ups • Investor relations • Strategy",
        matchPercent = "89%",
        verified = "Verified Builder",
        bio = "Two exits under my belt. Mentoring first-time founders; open to advisory or a founding CTO role.",
        interests = listOf("🚀 Scale", "💡 Mentorship", "🤝 Networks", "📍 Bengaluru", "💼 Advisor", "🏆 Exits")
    ),
    PartnerProfile(
        initial = "N",
        name = "Naina Kapoor",
        age = "27",
        role = "Community lead • Pune",
        location = "Pune • 15 km away",
        tagline = "Network building • User research • Ops",
        matchPercent = "90%",
        verified = "Verified Builder",
        bio = "Building developer communities. Looking for co-hosts and builder partners for regional hackathons.",
        interests = listOf("🎪 Events", "🧑‍🤝‍🧑 Community", "📣 DevRel", "📍 Pune", "🏆 Hackathons", "💼 Networking")
    )
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DiscoverScreen(onNavigateMatches: () -> Unit) {
    var selectedCategory by remember { mutableStateOf("Co-founders") }
    var showDetailsSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val categories = listOf("Hackathons", "Co-founders", "Startups", "Gigs")

    val deckIndex = remember { mutableStateOf(0) }
    val profile = sampleDeck[deckIndex.value % sampleDeck.size]
    val chipListState = rememberScrollState()
    val autoScrollEnabled = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (autoScrollEnabled.value) {
            delay(600)
            chipListState.animateScrollTo(
                value = chipListState.maxValue,
                animationSpec = tween(durationMillis = 1600, easing = LinearEasing)
            )
            delay(900)
            chipListState.animateScrollTo(
                value = 0,
                animationSpec = tween(durationMillis = 1600, easing = LinearEasing)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Discover your next\nbuild partner",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryText,
                    lineHeight = 34.sp
                )
                Text(
                    text = "AI-curated, verified, and ready to meet.",
                    fontSize = 13.sp,
                    color = SecondaryText
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(chipListState)
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                awaitFirstDown()
                                autoScrollEnabled.value = false
                            }
                        }
                    },
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = { Text(category, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CommunityBadge,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = PrimaryText
                        ),
                        shape = CircleShape
                    )
                }
            }

            FullPhotoCard(
                profile = profile,
                onViewDetails = { showDetailsSheet = true },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(70.dp))
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, CardBorderColor, CircleShape)
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    deckIndex.value = (deckIndex.value + 1) % sampleDeck.size
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PrimaryBackground)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Pass", tint = PrimaryText)
            }

            IconButton(
                onClick = onNavigateMatches,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PrimaryText)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Chat", tint = Color.White)
            }

            IconButton(
                onClick = {
                    deckIndex.value = (deckIndex.value + 1) % sampleDeck.size
                },
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(CommunityBadge)
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "Connect", tint = Color.White)
            }
        }

        if (showDetailsSheet) {
            ModalBottomSheet(
                onDismissRequest = { showDetailsSheet = false },
                sheetState = sheetState,
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                FullProfileDetailsContent(
                    profile = profile,
                    onClose = { showDetailsSheet = false }
                )
            }
        }
    }
}

@Composable
private fun FullPhotoCard(
    profile: PartnerProfile,
    onViewDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp)
                .offset(y = 10.dp),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE5DDD9))
        ) {}

        Card(
            modifier = Modifier
                .fillMaxSize()
                .shadow(12.dp, RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = CommunityBadge)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF4A121E),
                                    CommunityBadge,
                                    Color(0xFF2B0A12)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = profile.name.uppercase().substringBefore(" "),
                        color = Color.White.copy(alpha = 0.15f),
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "★ Top Pick",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${profile.matchPercent} Skill Match",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "${profile.name}, ${profile.age}",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF4CAF50))
                                )
                            }

                            Text(
                                text = profile.role,
                                fontSize = 15.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Medium
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = profile.location,
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }

                        IconButton(
                            onClick = onViewDetails,
                            modifier = Modifier
                                .size(44.dp)
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

                    Surface(
                        onClick = onViewDetails,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "View full profile & bio",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FullProfileDetailsContent(
    profile: PartnerProfile,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = profile.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText
                )
                Text(
                    text = profile.role,
                    fontSize = 14.sp,
                    color = SecondaryText
                )
            }
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PrimaryBackground)
            ) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Close", tint = PrimaryText)
            }
        }

        Divider(color = TagBgLight)

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(TagBgLight)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Skill Fit", fontSize = 11.sp, color = SecondaryText)
                    Text(profile.matchPercent, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CommunityBadge)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(TagBgLight)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Status", fontSize = 11.sp, color = SecondaryText)
                    Text(profile.verified, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryText)
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("About", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryText)
            Text(
                text = profile.bio,
                fontSize = 14.sp,
                color = SecondaryText,
                lineHeight = 22.sp
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Skills & Focus Areas", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryText)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                profile.interests.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(PrimaryBackground)
                            .border(1.dp, CardBorderColor, CircleShape)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(text = tag, fontSize = 13.sp, color = PrimaryText, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        Button(
            onClick = onClose,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = CommunityBadge)
        ) {
            Text("Connect with ${profile.name.substringBefore(" ")}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF7F2)
@Composable
fun DiscoverScreenPreview() {
    BriviaClubAppTheme {
        DiscoverScreen(onNavigateMatches = {})
    }
}