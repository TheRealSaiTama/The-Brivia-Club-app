package com.briviaclub.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ThreeDCard(modifier: Modifier = Modifier) {
    val density = LocalDensity.current.density
    val targetX = remember { mutableStateOf(10f) }
    val targetY = remember { mutableStateOf(-8f) }
    val rotationX by animateFloatAsState(
        targetValue = targetX.value,
        animationSpec = tween(durationMillis = 250)
    )
    val rotationY by animateFloatAsState(
        targetValue = targetY.value,
        animationSpec = tween(durationMillis = 250)
    )
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        targetX.value = (targetX.value + dragAmount.y / 10f).coerceIn(-15f, 20f)
                        targetY.value = (targetY.value - dragAmount.x / 10f).coerceIn(-20f, 15f)
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
                cameraDistance = 24f * density
            ),
        shape = RoundedCornerShape(32.dp),
        elevation = 18.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colors.surface,
                            MaterialTheme.colors.background
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Text(
                    text = "The Brivia Club",
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    text = "A 3D showcase of your next build partner.",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
