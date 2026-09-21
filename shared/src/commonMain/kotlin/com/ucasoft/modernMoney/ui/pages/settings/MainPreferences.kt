package com.ucasoft.modernMoney.ui.pages.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.viewModels.SettingsViewModel
import com.ucasoft.modern_money.shared.generated.resources.Res
import com.ucasoft.modern_money.shared.generated.resources.currencies
import com.ucasoft.modern_money.shared.generated.resources.language
import com.ucasoft.modern_money.shared.generated.resources.protect
import me.zhanghai.compose.preference.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainPreferences(
    viewModel: SettingsViewModel,
    onChildSettings: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProvidePreferenceLocals {
        Column {
            ListPreference(
                value = state.language,
                values = listOf("EN", "FR", "RU"),
                onValueChange = viewModel::setLanguage,
                title = { Text(stringResource(Res.string.language)) },
                summary = { Text(state.language) },
                type = ListPreferenceType.DROPDOWN_MENU
            )
            TwoTargetSwitchPreference(
                value = state.protect.enabled,
                onValueChange = { enabled ->
                    viewModel.setProtect(enabled)
                    if (enabled && !state.protect.isPinCodeSet) {
                        onChildSettings(SettingsPage.Protect.key)
                    }
                },
                title = { Text(stringResource(Res.string.protect)) },
                enabled = state.protect.enabled,
                switchEnabled = true,
                onClick = { onChildSettings(SettingsPage.Protect.key) }
            )
            Preference(
                title = { Text(stringResource(Res.string.currencies)) },
                onClick = { onChildSettings(SettingsPage.Currencies.key) }
            )
            ImportPreference()
        }
    }
}

