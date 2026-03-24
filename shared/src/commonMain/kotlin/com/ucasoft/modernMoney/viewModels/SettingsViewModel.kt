package com.ucasoft.modernMoney.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.getStringFlow
import com.russhwolf.settings.observable.makeObservable
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalSettingsApi::class)
class SettingsViewModel : ViewModel() {

    private val settings = Settings().makeObservable()

    private val language = settings.getStringFlow(SettingsKey.LANGUAGE, "EN")

    val state = combine(
        language
    ){ l ->
        SettingsState(l[0])
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SettingsState("EN")
    )

    fun setLanguage(language: String) {
        settings.putString(SettingsKey.LANGUAGE, language)
    }
}

data class SettingsState(
    val language: String
)

object SettingsKey {
    const val LANGUAGE = "language"
}