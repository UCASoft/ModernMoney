package com.ucasoft.modernMoney.ui.pages.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.viewModels.SettingsViewModel
import com.ucasoft.modern_money.shared.generated.resources.Res
import com.ucasoft.modern_money.shared.generated.resources.biometry
import com.ucasoft.modern_money.shared.generated.resources.pin_code
import kotlinx.coroutines.launch
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.SwitchPreference
import me.zhanghai.compose.preference.TextFieldPreference
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProtectPreferences(viewModel: SettingsViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    ProvidePreferenceLocals {
        Column {
            TextFieldPreference(
                enabled = state.protect.enabled,
                value = "",
                onValueChange = {
                    scope.launch {
                        viewModel.setProtectPinCode(it)
                    }
                },
                title = { Text(stringResource(Res.string.pin_code)) },
                textToValue = { it },
                summary = {
                    Text(if (state.protect.isPinCodeSet) "Defined" else "Not defined")
                },
                textField = PasswordFieldPreferenceDefaults.PasswordField
            )
            SwitchPreference(
                value = state.protect.biometryEnabled,
                onValueChange = {
                    scope.launch {
                        viewModel.setProtectBiometry(it)
                    }
                },
                title = { Text(stringResource(Res.string.biometry)) },
                enabled = false
            )
        }
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