package com.briviaclub.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.briviaclub.app.ui.components.ProfilePreviewCard
import com.briviaclub.app.ui.theme.CardBorder
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.PrimaryBackground
import com.briviaclub.app.ui.theme.PrimaryText
import com.briviaclub.app.ui.theme.SecondaryText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateProfileScreen(onSaveProfile: () -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var roleTitle by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val selectedSkills = remember { mutableStateListOf<String>() }
    val availableSkills = listOf("AI Products", "Launch Strategy", "Fundraising", "Fullstack", "UI/UX Design", "Growth")

    val selectedLookingFor = remember { mutableStateListOf<String>() }
    val lookingForOptions = listOf("Co-founder", "Teammates", "Mentorship", "Investors", "Networking")

    Surface(modifier = Modifier.fillMaxSize(), color = PrimaryBackground) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Build Your Profile",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText
                )
                Text(
                    text = "Showcase your builder identity to get curated matches.",
                    fontSize = 14.sp,
                    color = SecondaryText
                )
            }

            if (fullName.isNotBlank() || roleTitle.isNotBlank()) {
                ProfilePreviewCard(
                    name = fullName.ifBlank { "Your Name" },
                    role = roleTitle.ifBlank { "Your role • City" },
                    expertise = selectedSkills.take(2).joinToString(" • ").ifBlank { "Your skills" },
                    status = "Preview"
                )
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(CommunityBadge.copy(alpha = 0.1f))
                        .border(2.dp, CommunityBadge, CircleShape)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Photo",
                        tint = CommunityBadge,
                        modifier = Modifier.size(48.dp)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(CommunityBadge),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        focusedBorderColor = CommunityBadge,
                        unfocusedBorderColor = CardBorder,
                        focusedLabelColor = CommunityBadge,
                        cursorColor = CommunityBadge
                    )
                )

                OutlinedTextField(
                    value = roleTitle,
                    onValueChange = { roleTitle = it },
                    label = { Text("Role / Startup (e.g. Founder & Engineer)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        focusedBorderColor = CommunityBadge,
                        unfocusedBorderColor = CardBorder,
                        focusedLabelColor = CommunityBadge,
                        cursorColor = CommunityBadge
                    )
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location (e.g. Bengaluru, IN)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        focusedBorderColor = CommunityBadge,
                        unfocusedBorderColor = CardBorder,
                        focusedLabelColor = CommunityBadge,
                        cursorColor = CommunityBadge
                    )
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Your Expertise",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryText
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableSkills.forEach { skill ->
                        SelectablePill(
                            text = skill,
                            selected = selectedSkills.contains(skill),
                            onClick = {
                                if (selectedSkills.contains(skill)) {
                                    selectedSkills.remove(skill)
                                } else {
                                    selectedSkills.add(skill)
                                }
                            }
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Interested in / Looking For",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryText
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    lookingForOptions.forEach { option ->
                        SelectablePill(
                            text = option,
                            selected = selectedLookingFor.contains(option),
                            onClick = {
                                if (selectedLookingFor.contains(option)) {
                                    selectedLookingFor.remove(option)
                                } else {
                                    selectedLookingFor.add(option)
                                }
                            }
                        )
                    }
                }
            }

            Button(
                onClick = onSaveProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = CommunityBadge,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Complete Profile & Start Swiping",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SelectablePill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (selected) CommunityBadge else Color.White)
            .border(1.dp, if (selected) CommunityBadge else CardBorder, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selected) Color.White else PrimaryText
        )
    }
}
