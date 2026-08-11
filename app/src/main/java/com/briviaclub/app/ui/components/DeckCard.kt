package com.briviaclub.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.LightBackground
import com.briviaclub.app.ui.theme.LightSurface
import com.briviaclub.app.ui.theme.MutedBurgundy
import com.briviaclub.app.ui.theme.SoftGrey

@Composable
fun DeckCard(
    title: String,
    role: String,
    tags: List<String>,
    score: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(36.dp),
        backgroundColor = LightSurface,
        elevation = 16.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(LightSurface, LightBackground)
                    )
                )
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color.White.copy(alpha = 0.3f), Color.Transparent),
                            center = Offset.Zero,
                            radius = 260f
                        )
                    )
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color(0xFF6E1423), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title.first().toString(),
                            style = MaterialTheme.typography.h5,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = role,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }

                Text(
                    text = tags.joinToString(" • "),
                    style = MaterialTheme.typography.caption,
                    color = ChampagneGold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF4E6E1), RoundedCornerShape(22.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = score,
                        style = MaterialTheme.typography.body2,
                        color = MutedBurgundy,
                        fontWeight = FontWeight.SemiBold
                    )
                    Box(
                        modifier = Modifier
                            .background(Color.Transparent, RoundedCornerShape(16.dp))
                            .border(BorderStroke(1.dp, DeepWine.copy(alpha = 0.8f)), RoundedCornerShape(16.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "View",
                            style = MaterialTheme.typography.caption,
                            color = DeepWine,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
