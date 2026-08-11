package com.briviaclub.app.ui.screen

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.briviaclub.app.ui.theme.BriviaClubAppTheme
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.PrimaryBackground
import com.briviaclub.app.ui.theme.PrimaryText
import com.briviaclub.app.ui.theme.SecondaryText

private data class PartnerProfile(
    val initial: String,
    val name: String,
    val role: String,
    val tagline: String,
    val skillFit: String,
    val verified: String,
    val bio: String,
    val interests: List<String>
)

private val sampleDeck = listOf(
    PartnerProfile(
        initial = "A",
        name = "Ananya Rao",
        role = "Product Designer • NID",
        tagline = "Hackathon mentor • AI UX • Fintech",
        skillFit = "Skill fit: 94%",
        verified = "Verified Builder",
        bio = "Building AI-driven design systems. Looking for a fullstack co-founder to ship scalable MVP for B2B SaaS.",
        interests = listOf("🎨 Figma", "⚡ React", "🤖 LLMs", "📍 Bengaluru")
    ),
    PartnerProfile(
        initial = "D",
        name = "Dev Patel",
        role = "Full-stack engineer • IIT Bombay",
        tagline = "Node.js • Startup ops • Growth",
        skillFit = "Skill fit: 92%",
        verified = "Verified Builder",
        bio = "Shipped 3 MVPs in 18 months. Looking to pair with a sharp product/design mind for the next AI-native tool.",
        interests = listOf("⚛️ React", "🟢 Node", "☁️ AWS", "📍 Pune")
    ),
    PartnerProfile(
        initial = "R",
        name = "Rhea Sen",
        role = "Product Lead • Mumbai",
        tagline = "Design systems • Brand building • Growth",
        skillFit = "Skill fit: 91%",
        verified = "Verified Builder",
        bio = "Ex-fintech PM leading product discovery. Seeking a technical co-founder for a vertical SaaS experiment.",
        interests = listOf("📊 Metrics", "🧠 Strategy", "💼 SaaS", "📍 Mumbai")
    ),
    PartnerProfile(
        initial = "K",
        name = "Kabir Mehta",
        role = "Startup advisor • Bengaluru",
        tagline = "Scale-ups • Investor relations • Strategy",
        skillFit = "Skill fit: 89%",
        verified = "Verified Builder",
        bio = "Two exits under my belt. Mentoring first-time founders; open to advisory or a founding CTO role.",
        interests = listOf("🚀 Scale", "💡 Mentorship", "🤝 Networks", "📍 Bengaluru")
    ),
    PartnerProfile(
        initial = "N",
        name = "Naina Kapoor",
        role = "Community lead • Pune",
        tagline = "Network building • User research • Ops",
        skillFit = "Skill fit: 90%",
        verified = "Verified Builder",
        bio = "Building developer communities. Looking for co-hosts and builder partners for regional hackathons.",
        interests = listOf("🎪 Events", "🧑‍🤝‍🧑 Community", "📣 DevRel", "📍 Pune")
    )
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DiscoverScreen(onNavigateMatches: () -> Unit) {
    var selectedCategory by remember { mutableStateOf("Co-founders") }
    val categories = listOf("Hackathons", "Co-founders", "Startups", "Gigs")

    val deckIndex = remember { mutableStateOf(0) }
    val profile = sampleDeck[deckIndex.value % sampleDeck.size]

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
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Discover your next\nbuild partner",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryText,
                    lineHeight = 36.sp
                )
                Text(
                    text = "Swipe premium decks now — AI-curated, verified, and ready to meet.",
                    fontSize = 14.sp,
                    color = SecondaryText,
                    lineHeight = 20.sp
                )
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                text = category,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
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

            PartnerCard(profile = profile)

            Spacer(modifier = Modifier.height(70.dp))
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, Color(0xFFEFE8E2), CircleShape)
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
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Pass",
                    tint = PrimaryText
                )
            }

            IconButton(
                onClick = onNavigateMatches,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PrimaryText)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Message",
                    tint = Color.White
                )
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
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Connect",
                    tint = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PartnerCard(profile: PartnerProfile) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .offset(y = 12.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFE8E2)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {}

        Card(
            modifier = Modifier
                .fillMaxSize()
                .shadow(8.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
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
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(CommunityBadge),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = profile.initial,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column {
                            Text(
                                text = profile.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryText
                            )
                            Text(
                                text = profile.role,
                                fontSize = 13.sp,
                                color = SecondaryText
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFF3ECEE))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Top pick",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CommunityBadge
                        )
                    }
                }

                Text(
                    text = profile.tagline,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = ChampagneGold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(PrimaryBackground)
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.skillFit,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFF3ECEE))
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.verified,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CommunityBadge
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "About",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )
                    Text(
                        text = profile.bio,
                        fontSize = 13.sp,
                        color = SecondaryText,
                        lineHeight = 18.sp
                    )
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    profile.interests.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(PrimaryBackground)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 11.sp,
                                color = PrimaryText
                            )
                        }
                    }
                }
            }
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
