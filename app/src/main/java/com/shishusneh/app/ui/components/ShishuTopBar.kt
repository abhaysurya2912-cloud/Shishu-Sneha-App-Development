package com.shishusneh.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.shishusneh.app.ui.theme.Cream
import com.shishusneh.app.ui.theme.TextBrown

/**
 * Shared top app bar used on inner screens (Growth, Vaccination, Feeding, Milestones)
 * that sit within the bottom navigation scaffold. Provides a consistent back button
 * so users can always navigate up regardless of how they arrived.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShishuTopBar(
    title: String,
    onNavigateBack: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = TextBrown
            )
        },
        navigationIcon = {
            if (onNavigateBack != null) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back",
                        tint = TextBrown
                    )
                }
            }
        },
        actions = { actions() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = TextBrown,
            navigationIconContentColor = TextBrown
        )
    )
}
