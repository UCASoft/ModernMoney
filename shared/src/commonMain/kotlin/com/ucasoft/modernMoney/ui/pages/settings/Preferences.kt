package com.ucasoft.modernMoney.ui.pages.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.imports.ImportProvider
import com.ucasoft.modernMoney.imports.ImportStatus
import com.ucasoft.modernMoney.imports.ProgressStatus
import com.ucasoft.modernMoney.imports.money.MoneyJsonProvider
import com.ucasoft.modernMoney.ui.rememberJsonPicker
import com.ucasoft.modernMoney.viewModels.CurrenciesViewModel
import com.ucasoft.modernMoney.viewModels.SettingsViewModel
import com.ucasoft.modern_money.shared.generated.resources.Res
import com.ucasoft.modern_money.shared.generated.resources.currencies
import com.ucasoft.modern_money.shared.generated.resources.import_summary
import com.ucasoft.modern_money.shared.generated.resources.import_title
import com.ucasoft.modern_money.shared.generated.resources.language
import com.ucasoft.modern_money.shared.generated.resources.pin_code
import com.ucasoft.modern_money.shared.generated.resources.protect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import me.zhanghai.compose.preference.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Preferences() {
    val viewModel = koinViewModel<SettingsViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    var protected by remember { mutableStateOf(false) }

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
                value = protected,
                onValueChange = { protected = it },
                title = { Text(stringResource(Res.string.protect)) },
                enabled = protected,
                switchEnabled = true,
                onClick = { }
            )
            TextFieldPreference(
                value = "1234",
                onValueChange = { },
                title = { Text(stringResource(Res.string.pin_code)) },
                textToValue = { it },
                summary = { Text("Defined") },
                textField = PasswordFieldPreferenceDefaults.PasswordField
            )
            CurrenciesPreference()
            ImportPreference()
        }
    }
}

@Composable
fun ImportPreference() {
    val scope = rememberCoroutineScope()
    var importStatus by remember { mutableStateOf<ImportStatus<ProgressStatus<*>>>(ImportStatus.Idle()) }
    var importProviderName by rememberSaveable { mutableStateOf<String?>(null) }

    val providers = remember {
        listOf(
            MoneyJsonProvider("Money", "Import from Money JSON Backup")
        )
    }

    val jsonPicker = rememberJsonPicker( {
        val provider = providers.firstOrNull { it.name == importProviderName } ?: return@rememberJsonPicker
        scope.launch {
            provider.runImport(it).collect {
                importStatus = it
            }
        }
    })


    StatusListPreference(
        title = stringResource(Res.string.import_title),
        items = providers,
        itemLabel = { it.name },
        itemDescription = { it.description },
        status = importStatus,
        onItemSelected = {
            importProviderName = it.name
            jsonPicker()
        }
    )
}

@Composable
fun <T: ImportProvider<*>> StatusListPreference(
    title: String,
    items: List<T>,
    itemLabel: (T) -> String,
    itemDescription: (T) -> String,
    onItemSelected: (T) -> Unit,
    status: ImportStatus<ProgressStatus<*>>,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    val loadingStatus = status as? ImportStatus.Loading<*, *>
    val importProgress by remember(loadingStatus?.progressStatus) {
        loadingStatus?.progressStatus ?: emptyFlow()
    }.collectAsState(initial = null)
    val progressMessage = (importProgress as? Pair<*, *>)?.first as? String
    val progressPercent = ((importProgress as? Pair<*, *>)?.second as? Int)?.coerceIn(0, 100)

    Preference(
        title = { Text(title) },
        summary = {
            when (status) {
                is ImportStatus.Idle -> Text(stringResource(Res.string.import_summary))
                is ImportStatus.Loading<*, *> -> Text("Importing... ${progressMessage ?: "please wait"}")
                is ImportStatus.Success -> Text("Import completed successfully")
                is ImportStatus.Error -> Text("Error: ${status.message}")
            }
        },
        enabled = status !is ImportStatus.Loading<*, *>,
        widgetContainer = {
            if (status is ImportStatus.Loading<*, *>) {
                if (progressPercent != null) {
                    CircularProgressIndicator(
                        progress = { progressPercent / 100f },
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
            }
        },
        onClick = { showDialog = true },
        modifier = modifier
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(title) },
            text = {
                Column {
                    items.forEach { item ->
                        ListItem(
                            headlineContent = { Text(itemLabel(item)) },
                            supportingContent = { Text(itemDescription(item)) },
                            modifier = Modifier.clickable {
                                showDialog = false
                                onItemSelected(item)
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CurrenciesPreference() {

    val currenciesViewModel = koinViewModel<CurrenciesViewModel>()

    val currenciesState by currenciesViewModel.fullState.collectAsStateWithLifecycle()

    if (currenciesState.isLoading) {
        Preference(
            title = { Text(stringResource(Res.string.currencies)) },
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
