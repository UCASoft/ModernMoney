package com.ucasoft.modernMoney.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberImagePicker(onImageSelected: (ByteArray?) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        onImageSelected(uri?.let { context.contentResolver.openInputStream(it)?.readBytes() })
    }
    return remember {
        {
            launcher.launch("image/*")
        }
    }
}
