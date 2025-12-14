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
import androidx.navigation.NavController
import androidx.compose.foundation.clickable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest

private val LightBlue = Color(0xF0F8FFFF) // Very light blue
private val TitleBlue = Color(0xFF396882) // Matches your mockup

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun RequestListScreen(onCreateNewRequest: () -> Unit = {},
                      navController: NavController) {
    val viewModel: RequestListViewModel = hiltViewModel()

    val snackbarHostState = remember { SnackbarHostState() }
    val (snackbarMessage, setSnackbarMessage) = remember {
        androidx.compose.runtime.mutableStateOf<String?>(
            null
        )
    }
    val (snackbarColor, setSnackbarColor) = remember { androidx.compose.runtime.mutableStateOf(Color.Unspecified) }

    // Listen for request_result
    LaunchedEffect(navController) {
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("request_result")
            ?.observeForever { result ->
                if (result == "approve") {
                    setSnackbarMessage("Request approved")
                    setSnackbarColor(Color(0xFFA2DEB4)) // Light green
                } else if (result == "reject") {
                    setSnackbarMessage("Request rejected")
                    setSnackbarColor(Color(0xFFF0B7B7)) // Light red
                }
                navController.currentBackStackEntry?.savedStateHandle?.remove<String>("request_result")
            }
    }

    Box(Modifier.fillMaxSize()) {
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
        if (snackbarMessage != null) {
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Row(
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(snackbarColor)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = snackbarMessage,
                        color = Color(0xFF35564A),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "✕",
                        color = Color(0xCC000000),
                        fontSize = 18.sp,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { setSnackbarMessage(null) }
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRequestListScreen() {
    RequestListScreen(navController = rememberNavController())
}
