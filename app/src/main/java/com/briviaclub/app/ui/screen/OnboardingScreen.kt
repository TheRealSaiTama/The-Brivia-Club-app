package com.briviaclub.app.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.briviaclub.app.ui.theme.CardBorder
import com.briviaclub.app.ui.theme.CardSurface
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.PrimaryBackground
import com.briviaclub.app.ui.theme.PrimaryText
import com.briviaclub.app.ui.theme.SecondaryText

@Composable
fun OnboardingScreen(onContinue: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PrimaryBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CommunityBadge.copy(alpha = 0.1f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "EXCLUSIVE COMMUNITY",
                        color = CommunityBadge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Text(
                    text = "Welcome to\nThe Brivia Club",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryText,
                    lineHeight = 40.sp
                )

                Text(
                    text = "A members-only club for builders who want to swipe less and ship more.",
                    fontSize = 16.sp,
                    color = SecondaryText,
                    lineHeight = 22.sp
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CenterVisualSection()
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = CommunityBadge,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "Start building",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = "Membership subject to community approval",
                    fontSize = 12.sp,
                    color = SecondaryText.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun CenterVisualSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val avatarColors = listOf(
                Color(0xFF8B263E),
                Color(0xFFD4A373),
                Color(0xFF2A9D8F),
                Color(0xFFE76F51)
            )

            avatarColors.forEachIndexed { index, color ->
                Box(
                    modifier = Modifier
                        .offset(x = (-12 * index).dp)
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(3.dp, Color(0xFFFAF7F2), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "\u26A1",
                        fontSize = 20.sp
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = CardSurface,
            border = BorderStroke(1.dp, Color(0xFFF0E6E8)),
            elevation = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "12,400+ ",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = CommunityBadge
                    )
                    Text(
                        text = "builders",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )
                }
                Text(
                    text = "Verified members shaping products, startups, and engineering teams worldwide.",
                    fontSize = 14.sp,
                    color = SecondaryText,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
