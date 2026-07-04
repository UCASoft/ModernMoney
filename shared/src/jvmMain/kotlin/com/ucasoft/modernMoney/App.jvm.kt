package com.ucasoft.modernMoney

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale

actual object LocalAppLocale {
    @Composable
    actual infix fun provideLocale(language: String?): ProvidedValue<*> {
        Locale.setDefault(Locale.forLanguageTag(language))
        return staticCompositionLocalOf<String?>{ null } provides language
    }
}