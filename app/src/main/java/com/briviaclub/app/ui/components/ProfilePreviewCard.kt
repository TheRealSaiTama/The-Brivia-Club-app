package com.briviaclub.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.LightBackground
import com.briviaclub.app.ui.theme.LightSurface
import com.briviaclub.app.ui.theme.SoftGrey

@Composable
fun ProfilePreviewCard(
    name: String,
    role: String,
    expertise: String,
    status: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(32.dp),
        elevation = 18.dp,
        backgroundColor = LightSurface
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFFFFF7F1), LightSurface)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(DeepWine),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name.first().uppercase(),
                                style = MaterialTheme.typography.h4,
                                color = LightSurface
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.onBackground
                            )
                            Text(
                                text = role,
                                style = MaterialTheme.typography.body2,
                                color = SoftGrey
                            )
                            Text(
                                text = expertise,
                                style = MaterialTheme.typography.caption,
                                color = ChampagneGold
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .background(
                                color = DeepWine.copy(alpha = 0.14f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = status,
                            style = MaterialTheme.typography.caption,
                            color = DeepWine
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color.White.copy(alpha = 0.65f), Color(0xFFFFF2EA))
                                ),
                                shape = RoundedCornerShape(22.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Skill fit: 94%",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(DeepWine.copy(alpha = 0.14f), Color.Transparent)
                                ),
                                shape = RoundedCornerShape(22.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Top pick",
                            style = MaterialTheme.typography.caption,
                            color = DeepWine
                        )
                    }
                }
            }
        }
    }
}
