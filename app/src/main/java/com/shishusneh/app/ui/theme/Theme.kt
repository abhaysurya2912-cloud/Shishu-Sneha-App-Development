package com.shishusneh.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PeachPrimary,
    onPrimary = Cream,
    primaryContainer = PeachContainer,
    onPrimaryContainer = TextBrown,
    secondary = SoftGreen,
    onSecondary = Cream,
    secondaryContainer = SoftGreenContainer,
    onSecondaryContainer = TextBrown,
    tertiary = SoftPurple,
    onTertiary = Cream,
    tertiaryContainer = SoftPurpleContainer,
    onTertiaryContainer = TextBrown,
    background = Cream,
    onBackground = TextBrown,
    surface = CardWhite,
    onSurface = TextBrown,
    surfaceVariant = CreamDark,
    onSurfaceVariant = TextBrownLight,
    outline = PeachLight,
    outlineVariant = CreamDark,
    error = StatusDueSoon,
    onError = Cream,
    errorContainer = Color(0xFFFFE0DB),
    onErrorContainer = TextBrown
)

private val DarkColorScheme = darkColorScheme(
    primary = PeachLight,
    onPrimary = DarkBackground,
    primaryContainer = PeachDark,
    onPrimaryContainer = CreamDark,
    secondary = SoftGreenLight,
    onSecondary = DarkBackground,
    secondaryContainer = Color(0xFF1B5E20),
    onSecondaryContainer = SoftGreenLight,
    tertiary = SoftPurple,
    onTertiary = DarkBackground,
    tertiaryContainer = Color(0xFF4A148C),
    onTertiaryContainer = SoftPurpleContainer,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurface2,
    onSurfaceVariant = TextMuted,
    outline = TextGrey,
    outlineVariant = DarkSurface2,
    error = DarkError,
    onError = DarkBackground,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnSurface
)

@Composable
fun ShishuSnehTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ShishuTypography,
        content = content
    )
}
