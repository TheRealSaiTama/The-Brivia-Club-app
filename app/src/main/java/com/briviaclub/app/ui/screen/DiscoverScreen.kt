package com.briviaclub.app.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
        )
    )

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
                sampleDeck.firstOrNull()?.let { topProfile ->
                    ProfilePreviewCard(
                        name = topProfile.name,
                        role = topProfile.role,
                        expertise = topProfile.tags.take(2).joinToString(" • "),
                        status = "Top pick"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                SwipeDeck(deck = sampleDeck)
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onNavigateMatches,
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = DeepWine),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "View matches", color = WarmIvory)
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
