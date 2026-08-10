package com.briviaclub.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CharcoalWineBlack
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.RichBlack
import com.briviaclub.app.ui.theme.SoftTaupeGrey
import com.briviaclub.app.ui.theme.WarmIvory

@Composable
fun OnboardingScreen(onContinue: () -> Unit) {
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
                    text = "Welcome to The Brivia Club",
                    style = MaterialTheme.typography.h1,
                    color = WarmIvory
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "A members-only club for builders who want to swipe less and ship more.",
                    style = MaterialTheme.typography.body1,
                    color = SoftTaupeGrey
                )
                Spacer(modifier = Modifier.height(24.dp))
                StatCard(
                    title = "12,400+ builders",
                    subtitle = "Verified members shaping products and teams"
                )
            }
            Button(
                onClick = onContinue,
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = DeepWine),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Start building", color = WarmIvory)
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
                color = ChampagneGold
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
