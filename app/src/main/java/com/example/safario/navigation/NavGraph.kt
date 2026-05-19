package com.example.safario.navigation

import SignupScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.safario.ui.navigation.MainScreen
import com.example.safario.ui.screens.LoginScreen
import com.example.safario.ui.screens.SettingsScreen
import com.example.safario.ui.screens.SplashScreen

@Composable
fun NavGraph(
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        // SPLASH
        composable("splash") {

            SplashScreen(navController)
        }

        // LOGIN
        composable("login") {

            LoginScreen(navController)
        }

        // SIGNUP
        composable("signup") {

            SignupScreen(navController)
        }

        // SETTINGS
        composable("settings") {

            SettingsScreen(
                isDarkMode = isDarkMode,
                onThemeChange = onThemeChange
            )
        }

        // MAIN APP
        composable(
            route = "main/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val email =
                backStackEntry.arguments
                    ?.getString("email") ?: ""

            MainScreen(
                rootNavController = navController,
                userEmail = email,
                isDarkMode = isDarkMode,
                onThemeChange = onThemeChange
            )
        }
    }
}