package com.aryandi.requestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aryandi.requestapp.ui.theme.RequestAppTheme
import com.aryandi.requestapp.ui.RequestListScreen
import com.aryandi.requestapp.ui.RequestDetailScreen
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RequestAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        RequestListScreen(
                            onCreateNewRequest = { navController.navigate("detail") },
                            navController = navController
                        )
                    }
                    composable("detail") {
                        RequestDetailScreen(
                            onApproved = {
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "request_result",
                                    "approve"
                                )
                                navController.popBackStack()
                            },
                            onRejected = {
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "request_result",
                                    "reject"
                                )
                                navController.popBackStack()
                            },
                            onBack = {
                                navController.popBackStack()
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RequestAppTheme {
        Greeting("Android")
    }
}