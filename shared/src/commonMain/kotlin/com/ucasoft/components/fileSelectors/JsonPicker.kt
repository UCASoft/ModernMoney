package com.ucasoft.components.fileSelectors

import androidx.compose.runtime.Composable

@Composable
expect fun rememberJsonPicker(onJsonSelected: (SelectedFile<*>?) -> Unit): () -> Unit