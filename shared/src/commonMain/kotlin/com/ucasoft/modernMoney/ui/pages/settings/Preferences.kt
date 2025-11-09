package com.ucasoft.modernMoney.ui.pages.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.ucasoft.modernMoney.viewModels.CurrenciesViewModel
import me.zhanghai.compose.preference.ListPreference
import me.zhanghai.compose.preference.ListPreferenceType
import me.zhanghai.compose.preference.MultiSelectListPreference
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.TextFieldPreference
import me.zhanghai.compose.preference.TwoTargetSwitchPreference
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Preferences() {

    val settings = Settings()

    var language by remember { mutableStateOf(settings["language", "EN"]) }

    var protected by remember { mutableStateOf(false) }

    ProvidePreferenceLocals {
        Column {
            ListPreference(
                value = language,
                onValueChange = {
                    settings.putString("language", it)
                    language = it
                },
                values = listOf("EN", "FR"),
                title = { Text("Language") },
                summary = { Text(language) },
                type = ListPreferenceType.DROPDOWN_MENU
            )
            TwoTargetSwitchPreference(
                value = protected,
                onValueChange = { protected = it },
                title = { Text("Protected") },
                enabled = protected,
                switchEnabled = true,
                onClick = { }
            )
            TextFieldPreference(
                value = "1234",
                onValueChange = { },
                title = { Text("Pin Code") },
                textToValue = { it },
                summary = { Text("Defined") },
                textField = PasswordFieldPreferenceDefaults.PasswordField
            )
            CurrenciesPreference()
        }
    }
}

@Composable
fun CurrenciesPreference() {

    val currenciesViewModel = koinViewModel<CurrenciesViewModel>()

    val currenciesState by currenciesViewModel.fullState.collectAsStateWithLifecycle()

    if (currenciesState.isLoading) {
        Preference(
            title = { Text("Currencies") },
            enabled = false,
            widgetContainer = { CircularProgressIndicator() }
        )
    } else {
        MultiSelectListPreference(
            value = currenciesState.items.toSet(),
            onValueChange = {
                currenciesViewModel.updateCurrencies(it.toList())
            },
            values = currenciesState.remote,
            title = { Text("Currencies") },
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
}

internal object PasswordFieldPreferenceDefaults {
    val PasswordField:
            @Composable
                (value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit, onOk: () -> Unit) -> Unit =
        { value, onValueChange, onOk ->

            var visibility by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions { onOk() },
                singleLine = true,
                visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { visibility = !visibility }) {
                        Icon(
                            if (visibility) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                            ""
                        )
                    }
                }
            )
        }
}