package com.ucasoft.modernMoney.ui

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
actual fun rememberImagePicker(onImageSelected: (ByteArray?) -> Unit): () -> Unit {
    return rememberFilePicker(ActivityResultContracts.GetContent(), "image/*", onImageSelected)
}