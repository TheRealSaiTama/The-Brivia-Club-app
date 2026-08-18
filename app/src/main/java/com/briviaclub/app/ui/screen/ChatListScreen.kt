package com.briviaclub.app.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.briviaclub.app.data.local.entity.MatchEntity
import com.briviaclub.app.data.local.entity.UserEntity
import com.briviaclub.app.ui.components.BriviaTypographyLogo
import com.briviaclub.app.ui.theme.AmberGold
import com.briviaclub.app.ui.theme.ChampagneGold
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.DeepWine
import com.briviaclub.app.ui.theme.EmeraldVerified
import com.briviaclub.app.ui.viewmodel.BriviaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    viewModel: BriviaViewModel,
    onOpenChat: (matchId: String, partnerName: String, partnerInitial: String, partnerRole: String) -> Unit
) {
    val matches by viewModel.matches.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    var searchInbox by remember { mutableStateOf("") }

    val isDark = MaterialTheme.colorScheme.background.red < 0.2f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Spacer(modifier = Modifier.height(2.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BriviaTypographyLogo(isDark = isDark)

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(EmeraldVerified.copy(alpha = 0.15f))
                    .border(1.dp, EmeraldVerified.copy(alpha = 0.35f), CircleShape)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "${matches.size} Matches",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldVerified
                )
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchInbox,
            onValueChange = { searchInbox = it },
            placeholder = { Text("Search matches or messages...", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp)) },
            singleLine = true,
            shape = CircleShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = CommunityBadge,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        )

        // Top New Matches Horizontal Carousel
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "NEW BUILD MATCHES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = if (isDark) ChampagneGold else DeepWine
            )

            if (matches.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Text(
                        text = "Swipe right in Discover to form new matches.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(matches) { match ->
                        val partnerId = if (match.user1Id == (currentUser?.id ?: "me_builder_001")) match.user2Id else match.user1Id
                        val partnerUser by produceState<UserEntity?>(initialValue = null, partnerId) {
                            value = viewModel.getUserById(partnerId)
                        }

                        partnerUser?.let { partner ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable {
                                        onOpenChat(match.id, partner.name, partner.name.take(1), partner.role)
                                    }
                                    .padding(vertical = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, ChampagneGold, CircleShape)
                                ) {
                                    AsyncImage(
                                        model = partner.photoUrlsJson.ifBlank { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400" },
                                        contentDescription = partner.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(EmeraldVerified)
                                            .border(2.dp, Color.White, CircleShape)
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = partner.name.substringBefore(" "),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${match.matchScore}%",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldVerified
                                )
                            }
                        }
                    }
                }
            }
        }

        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

        // Messages List
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "CONVERSATIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (matches.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(42.dp)
                        )
                        Text(
                            text = "No active conversations yet",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Start swiping in Discover to meet fellow builders",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(matches) { match ->
                        val partnerId = if (match.user1Id == (currentUser?.id ?: "me_builder_001")) match.user2Id else match.user1Id
                        val partnerUser by produceState<UserEntity?>(initialValue = null, partnerId) {
                            value = viewModel.getUserById(partnerId)
                        }

                        partnerUser?.let { partner ->
                            androidx.compose.animation.AnimatedVisibility(
                                visible = true,
                                enter = androidx.compose.animation.fadeIn(androidx.compose.animation.core.tween(300)) +
                                    androidx.compose.animation.slideInVertically(
                                        initialOffsetY = { 30 },
                                        animationSpec = androidx.compose.animation.core.tween(300)
                                    )
                            ) {
                                MatchConversationItem(
                                    match = match,
                                    partner = partner,
                                    onClick = {
                                        onOpenChat(match.id, partner.name, partner.name.take(1), partner.role)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MatchConversationItem(
    match: MatchEntity,
    partner: UserEntity,
    onClick: () -> Unit
) {
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val timeStr = remember(match.lastMessageTime) { formatter.format(Date(match.lastMessageTime)) }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = partner.photoUrlsJson.ifBlank { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400" },
                    contentDescription = partner.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = partner.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (partner.isVerified) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = AmberGold,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Text(
                        text = timeStr,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = partner.role,
                    fontSize = 12.sp,
                    color = CommunityBadge,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = match.lastMessage,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (match.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(CommunityBadge),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = match.unreadCount.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
