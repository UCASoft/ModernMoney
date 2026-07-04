package com.ucasoft.modernMoney

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

actual object LocalAppLocale {

    @SuppressLint("ConstantLocale")
    private val default: Locale = Locale.getDefault()

    @Composable
    actual infix fun provideLocale(language: String?) : ProvidedValue<*> {
        val baseConfiguration = LocalConfiguration.current

        val locale = language?.let(Locale::forLanguageTag) ?: default
        Locale.setDefault(locale)

        val configuration = Configuration(baseConfiguration).apply {
            setLocale(locale)
        }

        val context = LocalContext.current
        val localizedContext = context.createConfigurationContext(configuration)

        return LocalContext provides localizedContext
    }
}
