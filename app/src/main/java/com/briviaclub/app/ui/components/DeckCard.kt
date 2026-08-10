package com.briviaclub.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CharcoalWineBlack
import com.briviaclub.app.ui.theme.MutedBurgundy
import com.briviaclub.app.ui.theme.RichBlack
import com.briviaclub.app.ui.theme.SoftTaupeGrey
import com.briviaclub.app.ui.theme.WarmIvory

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
        shape = RoundedCornerShape(28.dp),
        backgroundColor = CharcoalWineBlack,
        elevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(CharcoalWineBlack, RichBlack)
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    color = WarmIvory,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = role,
                    style = MaterialTheme.typography.body1,
                    color = SoftTaupeGrey
                )
                Text(
                    text = tags.joinToString(" • "),
                    style = MaterialTheme.typography.body2,
                    color = ChampagneGold
                )
                Text(
                    text = score,
                    style = MaterialTheme.typography.body1,
                    color = MutedBurgundy,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
