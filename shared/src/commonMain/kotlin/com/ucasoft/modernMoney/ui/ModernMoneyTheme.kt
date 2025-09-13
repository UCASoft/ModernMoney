package com.ucasoft.modernMoney.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ModernMoneyTheme(content: @Composable () -> Unit) {

    val positiveBalanceColor = Color(0xFF4FAB9A) // Teal-green for income/growth
    val negativeBalanceColor = Color(0xFFC03C15) // Red for expenses (Beyond Budget influence)
    val accountBlue = Color(0xFF504450)

    val lightColors = lightColorScheme(
        primary = Color(0xFF4FAB9A), // Teal primary
        primaryContainer = Color(0xFFE0F2F1),
        secondary = Color(0xFF504450), // Gray-blue secondary
        secondaryContainer = Color(0xFFEDE7F6),
        tertiary = Color(0xFFEAAC8A), // Orange for warnings/charts (Finch accents)
        background = Color(0xFFFFFFFF),
        surface = Color(0xFFFDFDFD),
        error = negativeBalanceColor,
        onPrimary = Color(0xFFFFFFFF),
        onSecondary = Color(0xFFFFFFFF),
        onBackground = Color(0xFF212121),
        onSurface = Color(0xFF212121),
        onError = Color(0xFFFFFFFF)
    )

// Dark Color Scheme: High-contrast for readability (Money Manager dark mode)
    val darkColors = darkColorScheme(
        primary = Color(0xFF80CBC4), // Lighter teal for visibility
        primaryContainer = Color(0xFF00695C),
        secondary = Color(0xFF9575CD), // Softer purple-blue for dark
        secondaryContainer = Color(0xFF4527A0),
        tertiary = Color(0xFFFFB74D), // Brighter orange
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        error = Color(0xFFEF5350),
        onPrimary = Color(0xFF000000),
        onSecondary = Color(0xFF000000),
        onBackground = Color(0xFFE0E0E0),
        onSurface = Color(0xFFE0E0E0),
        onError = Color(0xFF000000)
    )

    val isDarkTheme = isSystemInDarkTheme()

    MaterialTheme(
        if (isDarkTheme) darkColors else lightColors
    ) {
        content()
    }
}