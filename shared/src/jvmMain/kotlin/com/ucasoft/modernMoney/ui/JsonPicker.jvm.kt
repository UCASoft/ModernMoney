package com.ucasoft.modernMoney.ui

import androidx.compose.runtime.Composable

@Composable
actual fun rememberJsonPicker(onJsonSelected: (ByteArray?) -> Unit): () -> Unit {
    return rememberFilePicker("Select Money Backup", "*.json", onJsonSelected)
}