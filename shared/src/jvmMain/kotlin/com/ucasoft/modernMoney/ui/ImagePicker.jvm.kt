package com.ucasoft.modernMoney.ui

import androidx.compose.runtime.Composable

@Composable
actual fun rememberImagePicker(onImageSelected: (ByteArray?) -> Unit): () -> Unit {
    return rememberFilePicker("Select Logo", "*.png", onImageSelected)
}