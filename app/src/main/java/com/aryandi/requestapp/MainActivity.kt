package com.aryandi.requestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aryandi.requestapp.ui.theme.RequestAppTheme
import com.aryandi.requestapp.ui.RequestListScreen
import com.aryandi.requestapp.ui.RequestDetailScreen
import com.aryandi.requestapp.ui.Route
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RequestAppTheme {
                val backstack = rememberNavBackStack(Route.RequestList())
                
                NavDisplay(
                    backStack = backstack,
                    onBack = {
                        if (backstack.size > 1) {
                            backstack.removeAt(backstack.size - 1)
                        }
                    }
                ) { key ->
                    val route = key as Route
                    NavEntry(
                        key = route,
                        content = {
                            when (route) {
                                is Route.RequestList -> {
                                    RequestListScreen(
                                        result = route.result,
                                        onNavigateToDetail = { id ->
                                            backstack[0] = Route.RequestList(result = null)
                                            backstack.add(Route.RequestDetail(requestId = id))
                                        }
                                    )
                                }
                                is Route.RequestDetail -> {
                                    RequestDetailScreen(
                                        onResult = { result ->
                                            if (backstack.size > 1) {
                                                // Remove the detail screen
                                                backstack.removeAt(backstack.size - 1)
                                                // Update the RequestList route in the backstack with the result
                                                backstack[0] = Route.RequestList(result = result)
                                            }
                                        },
                                        onBack = {
                                            if (backstack.size > 1) {
                                                backstack.removeAt(backstack.size - 1)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

