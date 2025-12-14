package com.ucasoft.modernMoney.ui

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePicker(onImageSelected: (ByteArray?) -> Unit): () -> Unit
