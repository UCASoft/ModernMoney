package com.ucasoft.components.fileSelectors

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
actual fun rememberJsonPicker(onJsonSelected: (SelectedFile<*>?) -> Unit): () -> Unit {
    return rememberFilePicker(ActivityResultContracts.OpenDocument(), arrayOf("application/json"), onJsonSelected)
}