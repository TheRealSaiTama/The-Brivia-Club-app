package com.briviaclub.app.ui.screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.briviaclub.app.model.DeckCardData
import com.briviaclub.app.ui.components.DeckCard
import com.briviaclub.app.ui.components.ProfilePreviewCard
import com.briviaclub.app.ui.components.SwipeDeck
import com.briviaclub.app.ui.theme.BriviaClubAppTheme
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CharcoalWineBlack
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.SoftGrey
import com.briviaclub.app.ui.theme.WarmIvory

@Composable
fun DiscoverScreen(onNavigateMatches: () -> Unit) {
    val sampleDeck = listOf(
        DeckCardData(
            name = "Ananya Rao",
            role = "Product Designer • NID",
            tags = listOf("Hackathon mentor", "AI UX", "Fintech"),
            score = "Match score: 96%"
        ),
        DeckCardData(
            name = "Dev Patel",
            role = "Full-stack engineer • IIT Bombay",
            tags = listOf("Node.js", "Startup ops", "Growth"),
            score = "Match score: 92%"
        ),
        DeckCardData(
            name = "Rhea Sen",
            role = "Product Lead • Mumbai",
            tags = listOf("Design systems", "Brand building", "Growth"),
            score = "Match score: 94%"
        ),
        DeckCardData(
            name = "Kabir Mehta",
            role = "Startup advisor • Bangalore",
            tags = listOf("Scale-ups", "Investor relations", "Strategy"),
            score = "Match score: 91%"
        ),
        DeckCardData(
            name = "Naina Kapoor",
            role = "Community lead • Pune",
            tags = listOf("Network building", "User research", "Ops"),
            score = "Match score: 89%"
        )
    )

    val currentIndex = remember { mutableStateOf(0) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colors.background, MaterialTheme.colors.surface)
                    )
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Discover your next build partner",
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Swipe premium decks now — AI-curated, verified, and ready to meet.",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(22.dp))
                FilterRow(filters = listOf("Hackathons", "Co-founders", "Startups", "Gigs"))
                Spacer(modifier = Modifier.height(20.dp))
                sampleDeck.getOrNull(currentIndex.value)?.let { topProfile ->
                    ProfilePreviewCard(
                        name = topProfile.name,
                        role = topProfile.role,
                        expertise = topProfile.tags.take(2).joinToString(" • "),
                        status = "Top pick"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                SwipeDeck(
                    deck = sampleDeck,
                    currentIndex = currentIndex.value,
                    onSwipe = {
                        currentIndex.value = (currentIndex.value + 1).coerceAtMost(sampleDeck.size)
                    }
                )
                Spacer(modifier = Modifier.height(18.dp))
                ActionButtonRow(
                    onReject = {
                        currentIndex.value = (currentIndex.value + 1).coerceAtMost(sampleDeck.size)
                    },
                    onLike = {
                        currentIndex.value = (currentIndex.value + 1).coerceAtMost(sampleDeck.size)
                    }
                )
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onNavigateMatches,
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface, contentColor = DeepWine),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "View matches")
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F2ED)
@Composable
fun DiscoverScreenPreview() {
    BriviaClubAppTheme {
        DiscoverScreen(onNavigateMatches = {})
    }
}

@Composable
private fun ActionButtonRow(
    onReject: () -> Unit,
    onLike: () -> Unit
) {
    val rejectSource = remember { MutableInteractionSource() }
    val likeSource = remember { MutableInteractionSource() }
    val rejectPressed by rejectSource.collectIsPressedAsState()
    val likePressed by likeSource.collectIsPressedAsState()
    val rejectScale by animateFloatAsState(targetValue = if (rejectPressed) 0.94f else 1f, animationSpec = tween(120))
    val likeScale by animateFloatAsState(targetValue = if (likePressed) 0.94f else 1f, animationSpec = tween(120))
    val transition = rememberInfiniteTransition()
    val glowPulse by transition.animateFloat(
        initialValue = 0.18f,
        targetValue = 0.42f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onReject,
            modifier = Modifier
                .weight(1f)
                .scale(rejectScale),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, DeepWine.copy(alpha = 0.8f)),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent, contentColor = DeepWine),
            interactionSource = rejectSource,
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
        ) {
            Text(text = "✕ Reject")
        }
        Spacer(modifier = Modifier.width(14.dp))
        Button(
            onClick = onLike,
            modifier = Modifier
                .weight(1f)
                .scale(likeScale),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, DeepWine.copy(alpha = glowPulse)),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent, contentColor = DeepWine),
            interactionSource = likeSource,
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
        ) {
            Text(text = "❤ Like")
        }
    }
}

@Composable
private fun FilterRow(filters: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            Text(
                text = filter,
                color = DeepWine,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .background(
                        color = CharcoalWineBlack,
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            )
        }
    }
}
