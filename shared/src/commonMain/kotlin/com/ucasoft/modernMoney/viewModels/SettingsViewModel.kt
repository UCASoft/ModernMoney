package com.ucasoft.modernMoney.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import com.russhwolf.settings.coroutines.getStringFlow
import com.ucasoft.modernMoney.db.model.Currency as DbCurrency
import com.ucasoft.modernMoney.db.repositories.CurrencyRepository
import com.ucasoft.modernMoney.model.Currency
import com.ucasoft.modernMoney.model.toCurrency
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalSettingsApi::class)
class SettingsViewModel(
    private val settings: ObservableSettings,
    currencyRepository: CurrencyRepository
) : ViewModel() {

    private val language = settings.getStringFlow(SettingsKey.LANGUAGE, "EN")
    private val protect = settings.getBooleanFlow(SettingsKey.PROTECT, false)
    private val protectPinCode = settings.getStringOrNullFlow(SettingsKey.PROTECT_PIN_CODE)
    private val protectBiometry = settings.getBooleanFlow(SettingsKey.PROTECT_BIOMETRY, false)
    private val homeCurrencyCode = settings.getStringOrNullFlow(SettingsKey.HOME_CURRENCY)
    private val autoSwitching = settings.getBooleanFlow(SettingsKey.AUTO_SWITCHING, false)

    val state = combine(
        language, protect, protectPinCode, protectBiometry, homeCurrencyCode, currencyRepository.visibleCurrencies, autoSwitching
    ) { flows ->
        SettingsState(
            language = flows[0] as String,
            protect = ProtectState(
                enabled = flows[1] as Boolean,
                isPinCodeSet = !(flows[2] as String?).isNullOrBlank(),
                biometryEnabled = flows[3] as Boolean && !(flows[2] as String?).isNullOrBlank()
            ),
            currency = CurrencyState(
                homeCurrency = (flows[5] as List<DbCurrency>).find { it.code == flows[4] as String? }?.toCurrency(),
                autoSwitching = flows[6] as Boolean,
            )
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SettingsState("EN", ProtectState(), CurrencyState())
    )

    fun setLanguage(language: String) {
        settings.putString(SettingsKey.LANGUAGE, language)
    }

    fun setProtect(protect: Boolean) {
        settings.putBoolean(SettingsKey.PROTECT, protect)
    }

    suspend fun setProtectPinCode(pinCode: String) {
        val normalizedPinCode = pinCode.trim()
        if (normalizedPinCode.isEmpty()) {
            settings.putBoolean(SettingsKey.PROTECT, false)
            setProtectBiometry(false)
        }
    }

    fun setProtectBiometry(enabled: Boolean) {
        settings.putBoolean(SettingsKey.PROTECT_BIOMETRY, enabled)
    }

    fun setHomeCurrency(currency: Currency?) {
        if (currency != null) {
            settings.putString(SettingsKey.HOME_CURRENCY, currency.code)
        } else {
            settings.remove(SettingsKey.HOME_CURRENCY)
        }
    }

    fun setAutoSwitching(enabled: Boolean) {
        settings.putBoolean(SettingsKey.AUTO_SWITCHING, enabled)
    }

    fun resetHomeCurrency() {
        setHomeCurrency(null)
        setAutoSwitching(false)
    }

    fun disableIncompleteProtectSetup() {
        settings.putBoolean(SettingsKey.PROTECT, false)
        settings.putBoolean(SettingsKey.PROTECT_BIOMETRY, false)

    }
}

data class SettingsState(
    val language: String,
    val protect: ProtectState,
    val currency: CurrencyState
)

data class ProtectState(
    val enabled: Boolean = false,
    val isPinCodeSet: Boolean = false,
    val biometryEnabled: Boolean = false
) {
    val isConfigured: Boolean
        get() = enabled && isPinCodeSet

    val canUseBiometry: Boolean
        get() = isConfigured
}

data class CurrencyState(
    val homeCurrency: Currency? = null,
    val autoSwitching: Boolean = false
)

object SettingsKey {
    const val LANGUAGE = "language"
    const val PROTECT = "protect"
    const val PROTECT_PIN_CODE = "protect_pin_code"
    const val PROTECT_BIOMETRY = "protect_biometry"
    const val HOME_CURRENCY = "home_currency"

    const val AUTO_SWITCHING = "auto_switching"
}
