package com.ucasoft.components.fileSelectors

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
actual fun rememberImagePicker(onImageSelected: (SelectedFile<*>?) -> Unit): () -> Unit {
    return rememberFilePicker(ActivityResultContracts.GetContent(), "image/*", onImageSelected)
}