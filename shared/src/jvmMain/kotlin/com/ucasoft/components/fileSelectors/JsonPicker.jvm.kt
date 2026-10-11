package com.ucasoft.components.fileSelectors

import androidx.compose.runtime.Composable

@Composable
actual fun rememberJsonPicker(onJsonSelected: (SelectedFile<*>?) -> Unit): () -> Unit {
    return rememberFilePicker("Select Money Backup", "*.json", onJsonSelected)
}