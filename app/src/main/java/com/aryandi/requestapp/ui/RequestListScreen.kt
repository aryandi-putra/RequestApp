package com.aryandi.requestapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

private val LightBlue = Color(0xF0F8FFFF) // Very light blue
private val TitleBlue = Color(0xFF396882) // Matches your mockup

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun RequestListScreen(onCreateNewRequest: () -> Unit = {}) {
    val viewModel: RequestListViewModel = hiltViewModel()

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = LightBlue,
        content = { padding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp)
                    .padding(padding)
            ) {
                Text(
                    text = "Home",
                    fontSize = 36.sp,
                    color = TitleBlue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 24.dp)
                )
                Spacer(modifier = Modifier.height(48.dp))

                // Centered circular logo placeholder
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    // Replace with your Image composable when available
                    Text(
                        text = "Logo",
                        color = TitleBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp
                    )
                }
                Spacer(modifier = Modifier.height(72.dp))
                Button(
                    onClick = onCreateNewRequest,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = TitleBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Create new request", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewRequestListScreen() {
    RequestListScreen()
}
