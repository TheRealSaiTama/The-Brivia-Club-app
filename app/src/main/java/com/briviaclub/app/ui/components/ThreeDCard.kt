package com.briviaclub.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ThreeDCard(modifier: Modifier = Modifier) {
    val density = LocalDensity.current.density
    val targetX = remember { mutableStateOf(10f) }
    val targetY = remember { mutableStateOf(-8f) }
    val rotationX by animateFloatAsState(targetValue = targetX.value, animationSpec = tween(durationMillis = 300))
    val rotationY by animateFloatAsState(targetValue = targetY.value, animationSpec = tween(durationMillis = 300))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(340.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        targetX.value = (targetX.value + dragAmount.y / 12f).coerceIn(-18f, 18f)
                        targetY.value = (targetY.value - dragAmount.x / 12f).coerceIn(-18f, 18f)
                    },
                    onDragEnd = {
                        targetX.value = 10f
                        targetY.value = -8f
                    },
                    onDragCancel = {
                        targetX.value = 10f
                        targetY.value = -8f
                    }
                )
            }
            .graphicsLayer(
                rotationX = rotationX,
                rotationY = rotationY,
                cameraDistance = 30f * density
            ),
        shape = RoundedCornerShape(42.dp),
        elevation = 34.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFFFF3EE), Color.Transparent),
                        center = Offset.Zero,
                        radius = 420f
                    )
                )
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(Color(0xFF6E1423).copy(alpha = 0.12f))
            )
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.TopEnd)
                    .offset((-18).dp, 22.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary)
            )
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Text(
                    text = "Premium match",
                    style = MaterialTheme.typography.overline,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = "A curated swipe experience",
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.onBackground,
                    lineHeight = 36.sp
                )
                Text(
                    text = "Connect with verified builders, teams, and founders in a premium club-like flow.",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(MaterialTheme.colors.primary.copy(alpha = 0.18f), Color.Transparent)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Aayush Shahi",
                        style = MaterialTheme.typography.h1,
                        color = MaterialTheme.colors.onBackground
                    )
                    Text(
                        text = "Growth founder • Delhi",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(40.dp)
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colors.primary)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Swipe to connect",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}
