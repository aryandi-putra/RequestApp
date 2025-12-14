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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aryandi.requestapp.ui.NavKeys

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RequestAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavKeys.NavRoute.REQUEST_LIST
                ) {
                    composable(NavKeys.NavRoute.REQUEST_LIST) {
                        RequestListScreen(
                            onCreateNewRequest = { navController.navigate(NavKeys.NavRoute.REQUEST_DETAIL) },
                            navController = navController
                        )
                    }
                    composable(NavKeys.NavRoute.REQUEST_DETAIL) {
                        RequestDetailScreen(
                            onApproved = {
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    NavKeys.NavResult.REQUEST,
                                    NavKeys.NavEvent.APPROVE
                                )
                                navController.popBackStack()
                            },
                            onRejected = {
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    NavKeys.NavResult.REQUEST,
                                    NavKeys.NavEvent.REJECT
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

