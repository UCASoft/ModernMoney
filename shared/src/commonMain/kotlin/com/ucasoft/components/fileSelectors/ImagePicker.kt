package com.ucasoft.components.fileSelectors

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePicker(onImageSelected: (SelectedFile<*>?) -> Unit): () -> Unit
