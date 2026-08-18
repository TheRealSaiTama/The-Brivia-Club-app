package com.briviaclub.app.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.briviaclub.app.ui.theme.BriviaClubAppTheme
import com.briviaclub.app.ui.theme.CommunityBadge
import com.briviaclub.app.ui.theme.PrimaryBackground
import com.briviaclub.app.ui.theme.PrimaryText
import com.briviaclub.app.ui.theme.SecondaryText

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateProfileScreen(
    onBackClick: () -> Unit,
    onCompleteProfile: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var roleTitle by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var bioError by remember { mutableStateOf<String?>(null) }

    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> photoUri = uri }
    )

    val selectedSkills = remember { mutableStateListOf<String>() }
    val availableSkills = listOf("AI / ML", "SaaS", "Fullstack", "UI/UX", "Growth", "Marketing")

    val selectedLookingFor = remember { mutableStateListOf<String>() }
    val lookingForOptions = listOf("Co-founder", "Teammates", "Mentorship", "Investors")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground)
            .systemBarsPadding()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = PrimaryText
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Create Profile",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryText
            )
            Text(
                text = "Set up your builder identity to start matching with co-founders and teams.",
                fontSize = 14.sp,
                color = SecondaryText,
                lineHeight = 20.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(CommunityBadge.copy(alpha = 0.08f))
                    .border(2.dp, CommunityBadge, CircleShape)
                    .clickable {
                        imagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (photoUri != null) {
                    AsyncImage(
                        model = photoUri,
                        contentDescription = "Profile Photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Upload Photo",
                        tint = CommunityBadge,
                        modifier = Modifier.size(40.dp)
                    )
                }
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
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            OutlinedTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                    if (it.trim().isNotBlank()) nameError = null
                },
                label = { Text("Full Name *") },
                isError = nameError != null,
                supportingText = {
                    if (nameError != null) {
                        Text(nameError!!, color = Color(0xFFE53935))
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = CommunityBadge,
                    unfocusedBorderColor = Color(0xFFEFE6E8),
                    focusedLabelColor = CommunityBadge,
                    cursorColor = CommunityBadge
                )
            )

            OutlinedTextField(
                value = roleTitle,
                onValueChange = { roleTitle = it },
                label = { Text("Role / Headline (e.g. Founder, Developer)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = CommunityBadge,
                    unfocusedBorderColor = Color(0xFFEFE6E8),
                    focusedLabelColor = CommunityBadge,
                    cursorColor = CommunityBadge
                )
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location (e.g. Bengaluru)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = CommunityBadge,
                    unfocusedBorderColor = Color(0xFFEFE6E8),
                    focusedLabelColor = CommunityBadge,
                    cursorColor = CommunityBadge
                )
            )

            OutlinedTextField(
                value = bio,
                onValueChange = {
                    bio = it
                    if (it.trim().isNotBlank()) bioError = null
                },
                label = { Text("Short Bio / What are you building? *") },
                isError = bioError != null,
                supportingText = {
                    if (bioError != null) {
                        Text(bioError!!, color = Color(0xFFE53935))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                minLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = CommunityBadge,
                    unfocusedBorderColor = Color(0xFFEFE6E8),
                    focusedLabelColor = CommunityBadge,
                    cursorColor = CommunityBadge
                )
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Your Expertise",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableSkills.forEach { skill ->
                    val isSelected = selectedSkills.contains(skill)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) selectedSkills.remove(skill) else selectedSkills.add(skill)
                        },
                        label = { Text(skill) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CommunityBadge,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = PrimaryText
                        ),
                        shape = CircleShape
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Looking For",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                lookingForOptions.forEach { option ->
                    val isSelected = selectedLookingFor.contains(option)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) selectedLookingFor.remove(option) else selectedLookingFor.add(option)
                        },
                        label = { Text(option) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CommunityBadge,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = PrimaryText
                        ),
                        shape = CircleShape
                    )
                }
            }
        }

        Button(
            onClick = {
                var valid = true
                if (fullName.trim().isBlank()) {
                    nameError = "Full name cannot be empty"
                    valid = false
                }
                if (bio.trim().isBlank()) {
                    bioError = "Bio cannot be empty"
                    valid = false
                }
                if (valid) {
                    onCompleteProfile()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(vertical = 4.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = CommunityBadge,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Save Profile & Continue",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF7F2)
@Composable
fun CreateProfileScreenPreview() {
    BriviaClubAppTheme {
        CreateProfileScreen(onBackClick = {}, onCompleteProfile = {})
    }
}
