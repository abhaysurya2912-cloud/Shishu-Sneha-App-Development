package com.shishusneh.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object ProfileSetup : Screen("profile_setup")
    object BabyProfiles : Screen("baby_profiles")
    object LanguageSettings : Screen("language_settings")
    object Home : Screen("home")
    object Growth : Screen("growth")
    object Vaccination : Screen("vaccination")
    object Feeding : Screen("feeding")
    object Milestones : Screen("milestones")
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        screen = Screen.Home,
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        screen = Screen.Growth,
        label = "Growth",
        selectedIcon = Icons.Filled.TrendingUp,
        unselectedIcon = Icons.Outlined.TrendingUp
    ),
    BottomNavItem(
        screen = Screen.Vaccination,
        label = "Vaccines",
        selectedIcon = Icons.Filled.Vaccines,
        unselectedIcon = Icons.Outlined.Vaccines
    ),
    BottomNavItem(
        screen = Screen.Feeding,
        label = "Feeding",
        selectedIcon = Icons.Filled.LocalDrink,
        unselectedIcon = Icons.Outlined.LocalDrink
    ),
    BottomNavItem(
        screen = Screen.Milestones,
        label = "Milestones",
        selectedIcon = Icons.Filled.CheckCircle,
        unselectedIcon = Icons.Outlined.CheckCircle
    )
)
