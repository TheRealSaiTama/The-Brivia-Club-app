package com.briviaclub.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CharcoalWineBlack
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.MutedBurgundy
import com.briviaclub.app.ui.theme.RichBlack
import com.briviaclub.app.ui.theme.WarmIvory
import com.briviaclub.app.ui.theme.WineGoldEnd
import com.briviaclub.app.ui.theme.WineGoldStart

@Composable
fun HomeScreen(onNavigateDiscover: () -> Unit) {
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
                    text = "The Brivia Club",
                    style = MaterialTheme.typography.h1,
                    color = WarmIvory
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Swipe. Match. Build.",
                    style = MaterialTheme.typography.h2,
                    color = ChampagneGold
                )
                Spacer(modifier = Modifier.height(24.dp))
                StatCard(
                    title = "12,400+ builders",
                    subtitle = "Verified builders in the club"
                )
                Spacer(modifier = Modifier.height(16.dp))
                StatCard(
                    title = "640+ teams shipped",
                    subtitle = "Matched builders who launched together"
                )
                Spacer(modifier = Modifier.height(16.dp))
                StatCard(
                    title = "92% match satisfaction",
                    subtitle = "High signal matches, not noise"
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onNavigateDiscover,
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = DeepWine),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Start building", color = WarmIvory)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Browse the club", color = SoftTaupeGrey)
                }
            }
        }
    }
}

@Composable
private fun StatCard(title: String, subtitle: String) {
    Card(
        shape = RoundedCornerShape(20.dp),
        backgroundColor = CharcoalWineBlack,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.h2,
                color = ChampagneGold,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.body1,
                color = SoftTaupeGrey
            )
        }
    }
}
