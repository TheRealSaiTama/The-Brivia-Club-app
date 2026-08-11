package com.briviaclub.app.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.briviaclub.app.R
import com.briviaclub.app.ui.components.ProfilePreviewCard
import com.briviaclub.app.ui.components.ThreeDCard
import com.briviaclub.app.ui.theme.BriviaClubAppTheme
import com.briviaclub.app.ui.theme.CardBorder
import com.briviaclub.app.ui.theme.CardSurface
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.PrimaryBackground
import com.briviaclub.app.ui.theme.PrimaryText
import com.briviaclub.app.ui.theme.SecondaryText
import com.briviaclub.app.ui.theme.SoftGrey
import com.briviaclub.app.ui.theme.WarmIvory


@Composable
fun HomeScreen(onNavigateDiscover: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PrimaryBackground
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFFFFF9F6), Color.Transparent)
                            ),
                            shape = RoundedCornerShape(38.dp)
                        )
                        .padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .background(
                                color = CommunityBadge,
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "COMMUNITY",
                            style = MaterialTheme.typography.caption,
                            color = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 16.dp, y = (-22).dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(DeepWine.copy(alpha = 0.18f), Color.Transparent),
                                    center = Offset.Unspecified,
                                    radius = 140f
                                ),
                                shape = CircleShape
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 24.dp, y = (-8).dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(DeepWine.copy(alpha = 0.28f), Color.Transparent),
                                    center = Offset.Unspecified,
                                    radius = 80f
                                ),
                                shape = CircleShape
                            )
                    )
                    Column {
                        Text(
                            text = "Welcome to",
                            style = MaterialTheme.typography.body1,
                            color = SecondaryText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "The Brivia Club",
                            style = MaterialTheme.typography.h1.copy(letterSpacing = 0.8.sp),
                            color = PrimaryText
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "A premium swipe experience for founders, creators and teams.",
                            style = MaterialTheme.typography.body1,
                            color = SecondaryText
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        ProfilePreviewCard(
                            name = "Priya Sharma",
                            role = "Startup founder • Bengaluru",
                            expertise = "Launch strategy • AI products",
                            status = "Featured"
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(top = 4.dp)
                            .shadow(4.dp, RoundedCornerShape(20.dp), clip = false)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(DeepWine.copy(alpha = 0.18f), Color.Transparent)
                                ),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Premium club access",
                            style = MaterialTheme.typography.caption,
                            color = DeepWine
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(24.dp, RoundedCornerShape(32.dp), clip = false)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color(0xFFFFF6F2), Color.Transparent),
                                center = Offset.Unspecified,
                                radius = 420f
                            ),
                            shape = RoundedCornerShape(32.dp)
                        )
                        .padding(18.dp)
                ) {
                    ThreeDCard()
                }
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
                    colors = ButtonDefaults.buttonColors(backgroundColor = DeepWine, contentColor = WarmIvory),
                    elevation = ButtonDefaults.elevation(defaultElevation = 8.dp, pressedElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(text = "Start building")
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent, contentColor = MaterialTheme.colors.onBackground),
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Browse the club")
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F2ED)
@Composable
fun HomeScreenPreview() {
    BriviaClubAppTheme {
        HomeScreen(onNavigateDiscover = {})
    }
}

@Composable
private fun StatCard(title: String, subtitle: String) {
    Card(
        shape = RoundedCornerShape(20.dp),
        backgroundColor = CardSurface,
        border = BorderStroke(1.dp, CardBorder),
        elevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h2,
                    fontWeight = FontWeight.Bold,
                    color = DeepWine
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.body1,
                    color = SecondaryText
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_avatar_group),
                contentDescription = null,
                modifier = Modifier.size(52.dp)
            )
        }
    }
}
