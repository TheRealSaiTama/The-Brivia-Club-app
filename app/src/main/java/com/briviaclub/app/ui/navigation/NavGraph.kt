package com.briviaclub.app.ui.navigation

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.briviaclub.app.ui.screen.AnalyticsScreen
import com.briviaclub.app.ui.screen.ChatListScreen
import com.briviaclub.app.ui.screen.ChatScreen
import com.briviaclub.app.ui.screen.CreateProfileScreen
import com.briviaclub.app.ui.screen.DiscoverScreen
import com.briviaclub.app.ui.screen.MembershipScreen
import com.briviaclub.app.ui.screen.OnboardingScreen
import com.briviaclub.app.ui.screen.ProfileScreen
import com.briviaclub.app.ui.screen.SplashScreen
import com.briviaclub.app.ui.viewmodel.BriviaViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object CreateProfile : Screen("create_profile")
    object Main : Screen("main")
    object Chat : Screen("chat/{matchId}/{name}/{initial}/{role}") {
        fun createRoute(matchId: String, name: String, initial: String, role: String): String =
            "chat/${Uri.encode(matchId)}/${Uri.encode(name)}/${Uri.encode(initial)}/${Uri.encode(role)}"
    }
}

@Composable
fun BriviaNavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: BriviaViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(350))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(350))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        composable(
            route = Screen.Splash.route,
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onCompleteAuth = {
                    navController.navigate(Screen.CreateProfile.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.CreateProfile.route) {
            CreateProfileScreen(
                onBackClick = { navController.popBackStack() },
                onCompleteProfile = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.CreateProfile.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            MainContainerScreen(
                viewModel = viewModel,
                onNavigateChat = { matchId, name, initial, role ->
                    navController.navigate(Screen.Chat.createRoute(matchId, name, initial, role))
                },
                onNavigateOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                navArgument("matchId") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("initial") { type = NavType.StringType },
                navArgument("role") { type = NavType.StringType }
            ),
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(350))
            },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { it / 2 },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) { entry ->
            val matchId = entry.arguments?.getString("matchId").orEmpty()
            val name = entry.arguments?.getString("name").orEmpty()
            val initial = entry.arguments?.getString("initial").orEmpty()
            val role = entry.arguments?.getString("role").orEmpty()

            ChatScreen(
                matchId = matchId,
                partnerName = name,
                partnerInitial = initial,
                partnerRole = role,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun MainContainerScreen(
    viewModel: BriviaViewModel,
    onNavigateChat: (matchId: String, name: String, initial: String, role: String) -> Unit,
    onNavigateOnboarding: () -> Unit
) {
    var currentTab by remember { mutableStateOf(BottomTab.DISCOVER) }
    val matches by viewModel.matches.collectAsState()
    val unreadCount = matches.sumOf { it.unreadCount }

    Scaffold(
        bottomBar = {
            BriviaBottomNavBar(
                currentRoute = currentTab.route,
                unreadMatchesCount = unreadCount,
                onTabSelected = { selected ->
                    currentTab = selected
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(280, easing = FastOutSlowInEasing)))
                        .togetherWith(fadeOut(animationSpec = tween(200)))
                },
                label = "MainTabTransition"
            ) { tab ->
                when (tab) {
                    BottomTab.DISCOVER -> {
                        DiscoverScreen(
                            viewModel = viewModel,
                            onNavigateChat = onNavigateChat,
                            onNavigateUpgrade = { currentTab = BottomTab.MEMBERSHIP }
                        )
                    }
                    BottomTab.MATCHES -> {
                        ChatListScreen(
                            viewModel = viewModel,
                            onOpenChat = onNavigateChat
                        )
                    }
                    BottomTab.MEMBERSHIP -> {
                        MembershipScreen(
                            viewModel = viewModel,
                            onBack = { currentTab = BottomTab.DISCOVER }
                        )
                    }
                    BottomTab.PROFILE -> {
                        ProfileScreen(
                            viewModel = viewModel,
                            onNavigateUpgrade = { currentTab = BottomTab.MEMBERSHIP },
                            onNavigateOnboarding = onNavigateOnboarding
                        )
                    }
                    BottomTab.ANALYTICS -> {
                        AnalyticsScreen(
                            viewModel = viewModel,
                            onBack = { currentTab = BottomTab.DISCOVER }
                        )
                    }
                }
            }
        }
    }
}

