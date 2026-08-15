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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.briviaclub.app.ui.theme.BriviaClubAppTheme
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.PrimaryBackground
import com.briviaclub.app.ui.theme.PrimaryText
import com.briviaclub.app.ui.theme.SecondaryText

private data class InboxItem(
    val initial: String,
    val name: String,
    val role: String,
    val lastMessage: String,
    val time: String,
    val unread: Boolean
)

private val inboxItems = listOf(
    InboxItem(
        initial = "A",
        name = "Ananya Rao",
        role = "Product Designer",
        lastMessage = "Hey! Your portfolio is exactly what I was looking for.",
        time = "2m",
        unread = true
    ),
    InboxItem(
        initial = "D",
        name = "Dev Patel",
        role = "Fullstack Developer",
        lastMessage = "Send me the Figma file and I'll start on the API.",
        time = "1h",
        unread = true
    ),
    InboxItem(
        initial = "R",
        name = "Rhea Sen",
        role = "Growth Marketer",
        lastMessage = "It's a match! Let's set up the first sprint.",
        time = "3h",
        unread = false
    )
)

@Composable
fun MatchScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PrimaryBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(PrimaryBackground, Color.White)
                    )
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Inbox",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Chat with your build partners and set the first sprint goal.",
                fontSize = 13.sp,
                color = SecondaryText
            )
            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    inboxItems.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(DeepWine),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.initial,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = item.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryText,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                    if (item.unread) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(DeepWine)
                                        )
                                    }
                                }
                                Text(
                                    text = item.role,
                                    fontSize = 12.sp,
                                    color = SecondaryText
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.lastMessage,
                                    fontSize = 13.sp,
                                    color = if (item.unread) PrimaryText else SecondaryText,
                                    maxLines = 1
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = item.time,
                                fontSize = 12.sp,
                                color = SecondaryText
                            )
                        }

                        if (index != inboxItems.lastIndex) {
                            Divider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = Color(0xFFEFE6E8),
                                thickness = 1.dp
                            )
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