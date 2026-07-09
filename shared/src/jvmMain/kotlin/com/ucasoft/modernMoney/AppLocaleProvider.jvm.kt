package com.ucasoft.modernMoney

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import java.util.Locale

@Suppress("ConstantLocale")
private val defaultLocale: Locale = Locale.getDefault()

@Composable
actual fun AppLocaleProvider(language: String?, content: @Composable () -> Unit) {
    val locale = language?.let(Locale::forLanguageTag) ?: defaultLocale
    Locale.setDefault(locale)
    key(locale) {
        content()
    }
}
