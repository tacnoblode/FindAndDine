package com.howellkeatingfonseca.findanddine.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Light colour scheme built from the brand colours in Color.kt
private val LightColorScheme = lightColorScheme(
    primary = CharcoalPrimary,
    onPrimary = OnCharcoalPrimary,
    primaryContainer = CharcoalPrimaryContainer,
    onPrimaryContainer = OnCharcoalPrimaryContainer,
    secondary = GreySecondary,
    onSecondary = OnGreySecondary,
    secondaryContainer = GreySecondaryContainer,
    onSecondaryContainer = OnGreySecondaryContainer,
    tertiary = PaprikaTertiary,
    onTertiary = OnPaprikaTertiary,
    tertiaryContainer = PaprikaTertiaryContainer,
    onTertiaryContainer = OnPaprikaTertiaryContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightBackground,
    onSurface = LightOnBackground,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    surfaceTint = CharcoalPrimary,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    surfaceContainerLowest = LightSurfaceContainerLowest,
    surfaceContainerLow = LightSurfaceContainerLow,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest
)

// Dark colour scheme, used when the device is in dark mode
private val DarkColorScheme = darkColorScheme(
    primary = CharcoalPrimaryDark,
    onPrimary = OnCharcoalPrimaryDark,
    primaryContainer = CharcoalPrimaryContainerDark,
    onPrimaryContainer = OnCharcoalPrimaryContainerDark,
    secondary = GreySecondaryDark,
    onSecondary = OnGreySecondaryDark,
    secondaryContainer = GreySecondaryContainerDark,
    onSecondaryContainer = OnGreySecondaryContainerDark,
    tertiary = PaprikaTertiaryDark,
    onTertiary = OnPaprikaTertiaryDark,
    tertiaryContainer = PaprikaTertiaryContainerDark,
    onTertiaryContainer = OnPaprikaTertiaryContainerDark,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkBackground,
    onSurface = DarkOnBackground,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceTint = CharcoalPrimaryDark,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    surfaceContainerLowest = DarkSurfaceContainerLowest,
    surfaceContainerLow = DarkSurfaceContainerLow,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest
)

@Composable
fun FindAndDineTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic colour (Android 12+) is off by default so the app keeps its own brand colours
    // and looks the same on every device
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Picks the colour scheme based on dark mode and whether dynamic colour is enabled
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Applies the colours and typography to everything inside the theme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
