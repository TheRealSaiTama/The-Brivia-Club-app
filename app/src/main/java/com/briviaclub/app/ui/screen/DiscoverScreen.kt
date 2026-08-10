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
import androidx.compose.ui.unit.dp
import com.briviaclub.app.ui.components.DeckCard
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CharcoalWineBlack
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.RichBlack
import com.briviaclub.app.ui.theme.SoftTaupeGrey
import com.briviaclub.app.ui.theme.WarmIvory

@Composable
fun DiscoverScreen(onNavigateMatches: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = RichBlack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(RichBlack, CharcoalWineBlack)
                    )
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Discover your next build partner",
                    style = MaterialTheme.typography.h1,
                    color = WarmIvory
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Swipe premium decks now — AI-curated, verified, and ready to meet.",
                    style = MaterialTheme.typography.body1,
                    color = SoftTaupeGrey
                )
                Spacer(modifier = Modifier.height(22.dp))
                FilterRow(filters = listOf("Hackathons", "Co-founders", "Startups", "Gigs"))
                Spacer(modifier = Modifier.height(20.dp))
                DeckCard(
                    title = "Ananya Rao",
                    role = "Product Designer • NID",
                    tags = listOf("Hackathon mentor", "AI UX", "Fintech"),
                    score = "Match score: 96%"
                )
                DeckCard(
                    title = "Dev Patel",
                    role = "Full-stack engineer • IIT Bombay",
                    tags = listOf("Node.js", "Startup ops", "Growth"),
                    score = "Match score: 92%"
                )
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Divider(color = SoftTaupeGrey.copy(alpha = 0.2f))
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

@Composable
private fun FilterRow(filters: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            Text(
                text = filter,
                color = ChampagneGold,
                style = MaterialTheme.typography.body2,
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
