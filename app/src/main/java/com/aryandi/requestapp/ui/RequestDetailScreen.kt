package com.aryandi.requestapp.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

private val ScreenBlue = Color(0xFF436C78)
private val PanelBlue = Color(0xFF345966)
private val ApproveColor = Color(0xFFB8E6EB)
private val ApproveArrow = Color(0xFF3CA1AF)

@Composable
fun RequestDetailScreen(
    onApproved: () -> Unit = {},
    onRejected: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val viewModel: RequestDetailViewModel = hiltViewModel()
    val effect = viewModel.effect.collectAsState()
    val state = viewModel.state.collectAsState()
    val requestId = "REQ_1" // Example ID

    LaunchedEffect(effect.value) {
        when (effect.value) {
            is RequestDetailEffect.Approved -> onApproved()
            is RequestDetailEffect.Rejected -> onRejected()
            else -> {}
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(ScreenBlue)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Title
            Spacer(Modifier.height(32.dp))
            Text(
                text = "New Request",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(36.dp))
            // Panel
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PanelBlue)
                    .padding(18.dp)
            ) {
                Column {
                    Text(
                        text = state.value.header,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = state.value.body,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
            // State/Loading/Error Banner
            when (effect.value) {
                is RequestDetailEffect.Loading -> Text(
                    "Processing...",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                is RequestDetailEffect.Approved -> Text(
                    "Approved",
                    color = Color.Green,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                is RequestDetailEffect.Rejected -> Text(
                    "Rejected",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                is RequestDetailEffect.Error -> Text(
                    "Error Network",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                else -> {}
            }
            Spacer(Modifier.height(24.dp))
            // Buttons Row
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.handleAction(RequestDetailAction.Reject) },
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                    border = ButtonDefaults.outlinedButtonBorder,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = effect.value !is RequestDetailEffect.Loading
                ) {
                    Text(text = "Reject", color = Color.White)
                }
                SlideToApproveButton(
                    modifier = Modifier
                        .weight(2f)
                        .height(48.dp),
                    approved = false,
                    onComplete = { viewModel.handleAction(RequestDetailAction.Approve) }
                )
            }
        }
    }
}

@Composable
fun SlideToApproveButton(
    modifier: Modifier = Modifier,
    approved: Boolean = false,
    onComplete: () -> Unit = {}
) {
    // Slide button logic
    val offsetX = remember { mutableStateOf(0f) }
    val isApproved = remember { mutableStateOf(approved) }
    val maxOffset = 145.dp // Width handle travels
    val density = LocalDensity.current
    val animatedOffset =
        animateFloatAsState(targetValue = if (isApproved.value) with(density) { maxOffset.toPx() } else offsetX.value).value
    val buttonWidth = 220.dp

    Box(
        modifier = modifier
            .width(buttonWidth)
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isApproved.value) ApproveColor else Color(0xFF396882))
            .pointerInput(isApproved.value) {
                if (!isApproved.value) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        val next = (offsetX.value + dragAmount).coerceIn(
                            0f,
                            with(density) { maxOffset.toPx() })
                        offsetX.value = next
                        if (next >= with(density) { (maxOffset.toPx() - 10.dp.toPx()) }) {
                            isApproved.value = true
                            onComplete()
                        }
                    }
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        // Sliding handle
        Box(
            modifier = Modifier
                .offset { IntOffset(animatedOffset.toInt(), 0) }
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (isApproved.value) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Approved",
                    tint = ApproveArrow
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.KeyboardDoubleArrowRight,
                    contentDescription = "Slide arrow",
                    tint = ApproveArrow
                )
            }
        }
        // Text
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isApproved.value) "Approved" else "Slide to approve",
                color = if (isApproved.value) Color(0xFF396882) else Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRequestDetailScreen() {
    RequestDetailScreen(
        onApproved = {},
        onRejected = {},
        onBack = {}
    )
}
