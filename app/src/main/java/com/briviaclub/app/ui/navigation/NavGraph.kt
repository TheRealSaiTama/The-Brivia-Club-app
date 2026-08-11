package com.briviaclub.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.briviaclub.app.ui.screen.CreateProfileScreen
import com.briviaclub.app.ui.screen.DiscoverScreen
import com.briviaclub.app.ui.screen.HomeScreen
import com.briviaclub.app.ui.screen.MatchScreen
import com.briviaclub.app.ui.screen.OnboardingScreen

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object CreateProfile : Screen("create_profile")
    object Discover : Screen("discover")
    object Matches : Screen("matches")
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
            DiscoverScreen(onNavigateMatches = { navController.navigate(Screen.Matches.route) })
        }
        composable(Screen.Matches.route) {
            MatchScreen()
        }
    }
}
