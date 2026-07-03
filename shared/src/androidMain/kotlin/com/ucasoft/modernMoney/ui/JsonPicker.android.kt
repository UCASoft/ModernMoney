package com.ucasoft.modernMoney.ui

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
actual fun rememberJsonPicker(onJsonSelected: (ByteArray?) -> Unit): () -> Unit {
    return rememberFilePicker(ActivityResultContracts.OpenDocument(), arrayOf("application/json"), onJsonSelected)
}