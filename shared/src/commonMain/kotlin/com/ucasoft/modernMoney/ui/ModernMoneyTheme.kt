package com.ucasoft.modernMoney.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object FinanceLightColors {
    // Primary - Main brand color (Finance Blue)
    val Primary = Color(0xFF1E40AF)
    val OnPrimary = Color(0xFFFFFFFF)
    val PrimaryContainer = Color(0xFFDBEAFE)
    val OnPrimaryContainer = Color(0xFF001A41)

    // Secondary - Accent color (Success Green)
    val Secondary = Color(0xFF16A34A)
    val OnSecondary = Color(0xFFFFFFFF)
    val SecondaryContainer = Color(0xFFDCFCE7)
    val OnSecondaryContainer = Color(0xFF002106)

    // Tertiary - Supporting color (Warning Amber)
    val Tertiary = Color(0xFFD97706)
    val OnTertiary = Color(0xFFFFFFFF)
    val TertiaryContainer = Color(0xFFFEF3C7)
    val OnTertiaryContainer = Color(0xFF2D1500)

    // Error - Danger/Alert color
    val Error = Color(0xFFDC2626)
    val OnError = Color(0xFFFFFFFF)
    val ErrorContainer = Color(0xFFFEE2E2)
    val OnErrorContainer = Color(0xFF410002)

    // Background
    val Background = Color(0xFFFAFAFA)
    val OnBackground = Color(0xFF1A1C1E)

    // Surface
    val Surface = Color(0xFFFFFFFF)
    val OnSurface = Color(0xFF1A1C1E)
    val SurfaceVariant = Color(0xFFF3F4F6)
    val OnSurfaceVariant = Color(0xFF6B7280)

    // Outline
    val Outline = Color(0xFFD1D5DB)
    val OutlineVariant = Color(0xFFE5E7EB)

    // Additional Finance-specific colors
    val Income = Color(0xFF10B981)
    val Expense = Color(0xFFEF4444)
    val Investment = Color(0xFF8B5CF6)
    val Savings = Color(0xFF06B6D4)
}

object FinanceDarkColors {
    // Primary
    val Primary = Color(0xFF93C5FD)
    val OnPrimary = Color(0xFF003258)
    val PrimaryContainer = Color(0xFF004A77)
    val OnPrimaryContainer = Color(0xFFDBEAFE)

    // Secondary
    val Secondary = Color(0xFF86EFAC)
    val OnSecondary = Color(0xFF003910)
    val SecondaryContainer = Color(0xFF005319)
    val OnSecondaryContainer = Color(0xFFDCFCE7)

    // Tertiary
    val Tertiary = Color(0xFFFCD34D)
    val OnTertiary = Color(0xFF462B00)
    val TertiaryContainer = Color(0xFF654000)
    val OnTertiaryContainer = Color(0xFFFEF3C7)

    // Error
    val Error = Color(0xFFFCA5A5)
    val OnError = Color(0xFF690005)
    val ErrorContainer = Color(0xFF93000A)
    val OnErrorContainer = Color(0xFFFEE2E2)

    // Background
    val Background = Color(0xFF1A1C1E)
    val OnBackground = Color(0xFFE2E2E5)

    // Surface
    val Surface = Color(0xFF1F2937)
    val OnSurface = Color(0xFFE2E2E5)
    val SurfaceVariant = Color(0xFF374151)
    val OnSurfaceVariant = Color(0xFF9CA3AF)

    // Outline
    val Outline = Color(0xFF4B5563)
    val OutlineVariant = Color(0xFF374151)

    // Additional Finance-specific colors
    val Income = Color(0xFF34D399)
    val Expense = Color(0xFFF87171)
    val Investment = Color(0xFFA78BFA)
    val Savings = Color(0xFF22D3EE)
}

@Composable
fun ModernMoneyTheme(content: @Composable () -> Unit) {

    val lightColors = lightColorScheme(
        primary = FinanceLightColors.Primary,
        onPrimary = FinanceLightColors.OnPrimary,
        primaryContainer = FinanceLightColors.PrimaryContainer,
        onPrimaryContainer = FinanceLightColors.OnPrimaryContainer,

        secondary = FinanceLightColors.Secondary,
        onSecondary = FinanceLightColors.OnSecondary,
        secondaryContainer = FinanceLightColors.SecondaryContainer,
        onSecondaryContainer = FinanceLightColors.OnSecondaryContainer,

        tertiary = FinanceLightColors.Tertiary,
        onTertiary = FinanceLightColors.OnTertiary,
        tertiaryContainer = FinanceLightColors.TertiaryContainer,
        onTertiaryContainer = FinanceLightColors.OnTertiaryContainer,

        error = FinanceLightColors.Error,
        onError = FinanceLightColors.OnError,
        errorContainer = FinanceLightColors.ErrorContainer,
        onErrorContainer = FinanceLightColors.OnErrorContainer,

        background = FinanceLightColors.Background,
        onBackground = FinanceLightColors.OnBackground,

        surface = FinanceLightColors.Surface,
        onSurface = FinanceLightColors.OnSurface,
        surfaceVariant = FinanceLightColors.SurfaceVariant,
        onSurfaceVariant = FinanceLightColors.OnSurfaceVariant,

        outline = FinanceLightColors.Outline,
        outlineVariant = FinanceLightColors.OutlineVariant,
    )

    val darkColors = darkColorScheme(
        primary = FinanceDarkColors.Primary,
        onPrimary = FinanceDarkColors.OnPrimary,
        primaryContainer = FinanceDarkColors.PrimaryContainer,
        onPrimaryContainer = FinanceDarkColors.OnPrimaryContainer,

        secondary = FinanceDarkColors.Secondary,
        onSecondary = FinanceDarkColors.OnSecondary,
        secondaryContainer = FinanceDarkColors.SecondaryContainer,
        onSecondaryContainer = FinanceDarkColors.OnSecondaryContainer,

        tertiary = FinanceDarkColors.Tertiary,
        onTertiary = FinanceDarkColors.OnTertiary,
        tertiaryContainer = FinanceDarkColors.TertiaryContainer,
        onTertiaryContainer = FinanceDarkColors.OnTertiaryContainer,

        error = FinanceDarkColors.Error,
        onError = FinanceDarkColors.OnError,
        errorContainer = FinanceDarkColors.ErrorContainer,
        onErrorContainer = FinanceDarkColors.OnErrorContainer,

        background = FinanceDarkColors.Background,
        onBackground = FinanceDarkColors.OnBackground,

        surface = FinanceDarkColors.Surface,
        onSurface = FinanceDarkColors.OnSurface,
        surfaceVariant = FinanceDarkColors.SurfaceVariant,
        onSurfaceVariant = FinanceDarkColors.OnSurfaceVariant,

        outline = FinanceDarkColors.Outline,
        outlineVariant = FinanceDarkColors.OutlineVariant,
    )

    val isDarkTheme = isSystemInDarkTheme()

    MaterialTheme(
        if (isDarkTheme) darkColors else lightColors
    ) {
        content()
    }
}