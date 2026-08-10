package com.briviaclub.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CharcoalWineBlack
import com.briviaclub.app.ui.theme.RichBlack
import com.briviaclub.app.ui.theme.SoftTaupeGrey
import com.briviaclub.app.ui.theme.WarmIvory

@Composable
fun MatchScreen() {
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
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Your Matches",
                style = MaterialTheme.typography.h1,
                color = WarmIvory
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Where teams form and the next build begins.",
                style = MaterialTheme.typography.body1,
                color = SoftTaupeGrey
            )
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                shape = RoundedCornerShape(24.dp),
                backgroundColor = CharcoalWineBlack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "You've got a new match!",
                        style = MaterialTheme.typography.h2,
                        color = ChampagneGold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Open the team room, start the conversation, and set the first sprint goal.",
                        style = MaterialTheme.typography.body1,
                        color = SoftTaupeGrey
                    )
                }
            }
        }
    }
}
