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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.briviaclub.app.ui.theme.BriviaClubAppTheme
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CharcoalWineBlack
import com.briviaclub.app.ui.theme.RichBlack
import com.briviaclub.app.ui.theme.SoftTaupeGrey
import com.briviaclub.app.ui.theme.WarmIvory

@Composable
fun MatchScreen() {
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
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Your Matches",
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Where teams form and the next build begins.",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                shape = RoundedCornerShape(24.dp),
                backgroundColor = MaterialTheme.colors.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "You've got a new match!",
                        style = MaterialTheme.typography.h2,
                        color = DeepWine
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Open the team room, start the conversation, and set the first sprint goal.",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface, contentColor = DeepWine)
                        ) {
                            Text(text = "Message")
                        }
                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
                        ) {
                            Text(text = "View profile", color = MaterialTheme.colors.onBackground)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F2ED)
@Composable
fun MatchScreenPreview() {
    BriviaClubAppTheme {
        MatchScreen()
    }
}
