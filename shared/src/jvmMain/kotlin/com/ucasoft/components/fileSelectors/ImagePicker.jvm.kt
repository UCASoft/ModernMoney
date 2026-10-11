package com.ucasoft.components.fileSelectors

import androidx.compose.runtime.Composable

@Composable
actual fun rememberImagePicker(onImageSelected: (SelectedFile<*>?) -> Unit): () -> Unit {
    return rememberFilePicker("Select Logo", "*.png", onImageSelected)
}