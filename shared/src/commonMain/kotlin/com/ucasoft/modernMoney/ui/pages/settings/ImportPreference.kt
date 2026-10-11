package com.ucasoft.modernMoney.ui.pages.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ucasoft.components.fileSelectors.rememberJsonPicker
import com.ucasoft.modernMoney.imports.ImportPluginProcessor
import com.ucasoft.modernMoney.imports.ImportStatus
import com.ucasoft.modernMoney.imports.PercentageProgress
import com.ucasoft.modernMoney.imports.ProgressStatus
import com.ucasoft.modernMoney.plugin.interfaces.PluginManager
import com.ucasoft.modernMoney.plugin.interfaces.PluginType
import com.ucasoft.modernMoney.plugin.interfaces.imports.ImportPluginCallback
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportPluginData
import com.ucasoft.modernMoney.ui.toImageBitmap
import com.ucasoft.modern_money.shared.generated.resources.Res
import com.ucasoft.modern_money.shared.generated.resources.import_summary
import com.ucasoft.modern_money.shared.generated.resources.import_title
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import me.zhanghai.compose.preference.Preference
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ImportPreference() {
    val scope = rememberCoroutineScope()
    val pluginManager = koinInject<PluginManager>()
    var importStatus by remember { mutableStateOf<ImportStatus<ProgressStatus<*>>>(ImportStatus.Idle()) }
    var importProviderId by rememberSaveable { mutableStateOf<String?>(null) }

    val providers = remember(pluginManager) {
        pluginManager.loadPlugins()
            .filter { it.type == PluginType.IMPORT }
    }

    val jsonPicker = rememberJsonPicker({
        if (it != null) {
            val provider = providers.firstOrNull { it.packageName == importProviderId } ?: return@rememberJsonPicker
            scope.launch {
                ImportPluginProcessor().runImport(provider, it, scope).collect {
                    importStatus = it
                }
            }
        }
    })


    StatusListPreference(
        title = stringResource(Res.string.import_title),
        items = providers,
        itemLabel = { it.title },
        itemDescription = { it.description },
        status = importStatus,
        icon = {
            Image(
                bitmap = it.icon.toImageBitmap(),
                contentDescription = null
            )
        },
        onItemSelected = {
            importProviderId = it.packageName
            jsonPicker()
        }
    )
}

@Composable
fun <T> StatusListPreference(
    title: String,
    items: List<T>,
    itemLabel: (T) -> String,
    itemDescription: (T) -> String,
    onItemSelected: (T) -> Unit,
    status: ImportStatus<ProgressStatus<*>>,
    icon: @Composable ((T) -> Unit)? = null,
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
                        progress = { progressPercent / 100f }
                    )
                } else {
                    CircularProgressIndicator()
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
                            leadingContent = if (icon != null) {
                                { icon(item) }
                            } else null,
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
