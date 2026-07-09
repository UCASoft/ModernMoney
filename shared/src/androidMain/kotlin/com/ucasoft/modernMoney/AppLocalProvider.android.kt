package com.ucasoft.modernMoney

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@SuppressLint("ConstantLocale")
private val defaultLocale: Locale = Locale.getDefault()

@Composable
actual fun AppLocaleProvider(language: String?, content: @Composable () -> Unit) {
    val baseConfiguration = LocalConfiguration.current
    val activityResultRegistryOwner = LocalActivityResultRegistryOwner.current

    val locale = language?.let(Locale::forLanguageTag) ?: defaultLocale
    Locale.setDefault(locale)

    val configuration = Configuration(baseConfiguration).apply {
        setLocale(locale)
    }

    val context = LocalContext.current
    val localizedContext = context.createConfigurationContext(configuration)

    if (activityResultRegistryOwner != null) {
        CompositionLocalProvider(
            LocalContext provides localizedContext,
            LocalActivityResultRegistryOwner provides activityResultRegistryOwner,
            content = content
        )
    } else {
        CompositionLocalProvider(
            LocalContext provides localizedContext,
            content = content
        )
    }
}
