package com.ucasoft.modernMoney.ui.pages.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.viewModels.CurrenciesViewModel
import com.ucasoft.modernMoney.viewModels.SettingsViewModel
import com.ucasoft.modern_money.shared.generated.resources.Res
import com.ucasoft.modern_money.shared.generated.resources.currencies
import me.zhanghai.compose.preference.ListPreference
import me.zhanghai.compose.preference.ListPreferenceType
import me.zhanghai.compose.preference.MultiSelectListPreference
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.SwitchPreference
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CurrenciesPreference(viewModel: SettingsViewModel) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val homeCurrency = state.currency.homeCurrency

    val currenciesViewModel = koinViewModel<CurrenciesViewModel>()
    val fullCurrenciesState by currenciesViewModel.fullState.collectAsStateWithLifecycle()
    val visibleCurrenciesState by currenciesViewModel.visibleState.collectAsStateWithLifecycle()

    ProvidePreferenceLocals {
        Column {
            if (fullCurrenciesState.isLoading) {
                Preference(
                    title = { Text(stringResource(Res.string.currencies)) },
                    enabled = false,
                    widgetContainer = { CircularProgressIndicator() }
                )
            } else {
                MultiSelectListPreference(
                    value = fullCurrenciesState.items.toSet(),
                    onValueChange = {
                        val currencies = it.toList()
                        currenciesViewModel.updateCurrencies(currencies)
                        if (homeCurrency != null && !currencies.contains(homeCurrency)) {
                            viewModel.resetHomeCurrency()
                        }
                    },
                    values = fullCurrenciesState.remote,
                    title = { Text(stringResource(Res.string.currencies)) },
                    item = { v, vs, t ->
                        ListItem(
                            modifier = Modifier.toggleable(v in vs, true, Role.Checkbox, null, t),
                            leadingContent = {
                                Checkbox(
                                    checked = v in vs,
                                    onCheckedChange = null
                                )
                            },
                            headlineContent = { Text(v.name) },
                            supportingContent = { Text(v.code) },
                            trailingContent = { Text(v.symbol) }
                        )
                    }
                )
            }
            ListPreference(
                value = homeCurrency,
                values = visibleCurrenciesState.items,
                valueToText = { AnnotatedString(it?.name ?: "") },
                enabled = !fullCurrenciesState.isLoading && visibleCurrenciesState.items.isNotEmpty(),
                onValueChange = {
                    viewModel.setHomeCurrency(it)
                },
                title = { Text("Home Currency") },
                summary = { Text(homeCurrency?.name ?: "") },
                type = ListPreferenceType.DROPDOWN_MENU
            )
            SwitchPreference(
                value = state.currency.autoSwitching,
                onValueChange = {
                    viewModel.setAutoSwitching(it)
                },
                enabled = !fullCurrenciesState.isLoading && homeCurrency != null,
                title = { Text("Auto Change Home Currency based on location") }
            )
        }
    }
}