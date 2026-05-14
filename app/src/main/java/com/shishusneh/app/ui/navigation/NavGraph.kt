package com.shishusneh.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shishusneh.app.ui.components.ShishuTopBar
import com.shishusneh.app.ui.screens.feeding.FeedingGuideScreen
import com.shishusneh.app.ui.screens.growth.GrowthTrackerScreen
import com.shishusneh.app.ui.screens.home.HomeScreen
import com.shishusneh.app.ui.screens.milestones.MilestoneScreen
import com.shishusneh.app.ui.screens.profile.BabyProfileListScreen
import com.shishusneh.app.ui.screens.profile.ProfileSetupScreen
import com.shishusneh.app.ui.screens.settings.LanguageSettingsScreen
import com.shishusneh.app.ui.screens.splash.SplashScreen
import com.shishusneh.app.ui.screens.vaccination.VaccinationScreen
import com.shishusneh.app.viewmodel.ProfileViewModel

@Composable
fun ShishuSnehNavGraph() {
    val navController = rememberNavController()
    val profileViewModel: ProfileViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarRoutes = listOf(
        Screen.Home.route,
        Screen.Growth.route,
        Screen.Vaccination.route,
        Screen.Feeding.route,
        Screen.Milestones.route
    )
    val showBottomBar = currentDestination?.route in bottomBarRoutes

    // Show a top bar with back nav on non-home bottom-bar screens
    val innerScreenRoute = currentDestination?.route
    val showTopBar = innerScreenRoute in listOf(
        Screen.Growth.route,
        Screen.Vaccination.route,
        Screen.Feeding.route,
        Screen.Milestones.route
    )
    val topBarTitle = when (innerScreenRoute) {
        Screen.Growth.route      -> "Growth Tracker"
        Screen.Vaccination.route -> "Vaccine Schedule"
        Screen.Feeding.route     -> "Feeding Guide"
        Screen.Milestones.route  -> "Milestones"
        else                     -> ""
    }

    Scaffold(
        topBar = {
            // ── FIX #13: back-navigation top bar on inner tab screens ──
            if (showTopBar) {
                ShishuTopBar(
                    title = topBarTitle,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.screen.route
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(item.label, style = MaterialTheme.typography.labelSmall)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.ProfileSetup.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                    profileViewModel = profileViewModel
                )
            }
            composable(Screen.ProfileSetup.route) {
                ProfileSetupScreen(
                    onProfileSaved = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.ProfileSetup.route) { inclusive = true }
                        }
                    },
                    viewModel = profileViewModel
                )
            }
            composable(Screen.BabyProfiles.route) {
                BabyProfileListScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAddProfile = { navController.navigate(Screen.ProfileSetup.route) },
                    onProfileSelected = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    viewModel = profileViewModel
                )
            }
            composable(Screen.LanguageSettings.route) {
                LanguageSettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.Growth.route) {
                GrowthTrackerScreen()
            }
            composable(Screen.Vaccination.route) {
                VaccinationScreen()
            }
            composable(Screen.Feeding.route) {
                FeedingGuideScreen()
            }
            composable(Screen.Milestones.route) {
                MilestoneScreen()
            }
        }
    }
}
