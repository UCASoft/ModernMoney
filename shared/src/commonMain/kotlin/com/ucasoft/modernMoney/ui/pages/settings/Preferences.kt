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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import me.zhanghai.compose.preference.ListPreference
import me.zhanghai.compose.preference.ListPreferenceType
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.TextFieldPreference
import me.zhanghai.compose.preference.TwoTargetSwitchPreference

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