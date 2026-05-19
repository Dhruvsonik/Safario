package com.example.safario.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.safario.ui.components.BottomNavBar
import com.example.safario.ui.screens.AIChatScreen
import com.example.safario.ui.screens.ContactScreen
import com.example.safario.ui.screens.HomeScreen
import com.example.safario.ui.screens.LoginScreen
import com.example.safario.ui.screens.MapScreen
import com.example.safario.ui.screens.ProfileScreen
import com.example.safario.ui.screens.SosScreen
import com.example.safario.ui.screens.TrackingScreen
import com.example.safario.utils.SessionManager

@Composable
fun MainScreen(
    rootNavController: NavController,
    userEmail: String,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {

    val navController = rememberNavController()

    val navBackStackEntry =
        navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry.value
            ?.destination
            ?.route ?: ""

    Scaffold(
        containerColor = Color(0xFF1B0F4B),
        bottomBar = {

            BottomNavBar(
                navController = navController,

                currentRoute =
                    when {

                        currentRoute.startsWith("home")
                            -> "home"

                        currentRoute.startsWith("map")
                            -> "map"

                        currentRoute.startsWith("sos")
                            -> "sos"

                        currentRoute.startsWith("ai")
                            -> "ai"

                        currentRoute.startsWith("profile")
                            -> "profile"

                        else -> "home"
                    },

                userEmail = userEmail
            )
        }

    ) { innerPadding ->

        NavHost(
            navController = navController,

            startDestination = "home/$userEmail",

            modifier = Modifier.padding(innerPadding)
        ) {

            // HOME
            composable(
                route = "home/{email}"
            ) { backStackEntry ->

                val email =
                    backStackEntry.arguments
                        ?.getString("email") ?: ""

                HomeScreen(
                    navController = navController,
                    userEmail = email
                )
            }

            // MAP
            composable(
                route =
                    "map/{email}" +
                            "?lat={lat}" +
                            "&lng={lng}" +
                            "&name={name}",

                arguments = listOf(

                    navArgument("email") {
                        type = NavType.StringType
                    },

                    navArgument("lat") {
                        nullable = true
                        defaultValue = null
                        type = NavType.StringType
                    },

                    navArgument("lng") {
                        nullable = true
                        defaultValue = null
                        type = NavType.StringType
                    },

                    navArgument("name") {
                        nullable = true
                        defaultValue = null
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val email =
                    backStackEntry.arguments
                        ?.getString("email") ?: ""

                val lat =
                    backStackEntry.arguments
                        ?.getString("lat")
                        ?.toDoubleOrNull()

                val lng =
                    backStackEntry.arguments
                        ?.getString("lng")
                        ?.toDoubleOrNull()

                val name =
                    backStackEntry.arguments
                        ?.getString("name")

                MapScreen(
                    navController = navController,
                    lat = lat,
                    lng = lng,
                    name = name,
                    userEmail = email
                )
            }

            // SOS
            composable(
                route = "sos/{email}"
            ) { backStackEntry ->

                val email =
                    backStackEntry.arguments
                        ?.getString("email") ?: ""

                SosScreen(
                    navController = navController,
                    userEmail = email
                )
            }

            // PROFILE
            composable(
                route = "profile/{email}"
            ) {

                val context = LocalContext.current

                val session =
                    SessionManager(context)

                ProfileScreen(
                    isDarkMode = isDarkMode,

                    onThemeChange = onThemeChange,

                    onLogout = {

                        session.clearSession()

                        rootNavController.navigate("login") {

                            popUpTo(0)

                            launchSingleTop = true
                        }
                    },

                    rootNavController = rootNavController
                )
            }

            // CONTACTS
            composable("contacts") {

                ContactScreen()
            }

            // TRACKING
            composable("tracking") {

                TrackingScreen()
            }

            // AI
            composable(
                route = "ai/{email}"
            ) { backStackEntry ->

                val email =
                    backStackEntry.arguments
                        ?.getString("email") ?: ""

                AIChatScreen(
                    navController = navController,
                    userEmail = email
                )
            }

            // LOGIN
            composable("login") {

                LoginScreen(rootNavController)
            }
        }
    }
}