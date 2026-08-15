package com.briviaclub.app.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.briviaclub.app.ui.screen.ChatScreen
import com.briviaclub.app.ui.screen.CreateProfileScreen
import com.briviaclub.app.ui.screen.DiscoverScreen
import com.briviaclub.app.ui.screen.HomeScreen
import com.briviaclub.app.ui.screen.OnboardingScreen

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object CreateProfile : Screen("create_profile")
    object Discover : Screen("discover")
    object Chat : Screen("chat/{initial}/{name}/{role}") {
        fun createRoute(initial: String, name: String, role: String): String =
            "chat/${Uri.encode(initial)}/${Uri.encode(name)}/${Uri.encode(role)}"
    }
}

@Composable
fun BriviaNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Onboarding.route) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(onContinue = { navController.navigate(Screen.Home.route) })
        }
        composable(Screen.Home.route) {
            HomeScreen(onNavigateCreateProfile = { navController.navigate(Screen.CreateProfile.route) })
        }
        composable(Screen.CreateProfile.route) {
            CreateProfileScreen(
                onBackClick = { navController.popBackStack() },
                onCompleteProfile = {
                    navController.navigate(Screen.Discover.route) {
                        popUpTo(Screen.CreateProfile.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Discover.route) {
            DiscoverScreen(
                onNavigateChat = { name, initial, role ->
                    navController.navigate(Screen.Chat.createRoute(initial, name, role))
                }
            )
        }
        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                navArgument("initial") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("role") { type = NavType.StringType }
            )
        ) { entry ->
            ChatScreen(
                name = entry.arguments?.getString("name").orEmpty(),
                initial = entry.arguments?.getString("initial").orEmpty(),
                role = entry.arguments?.getString("role").orEmpty(),
                onBack = { navController.popBackStack() }
            )
        }
    }
}