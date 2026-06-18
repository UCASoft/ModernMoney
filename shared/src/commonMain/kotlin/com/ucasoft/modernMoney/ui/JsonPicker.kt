package com.ucasoft.modernMoney.ui

import androidx.compose.runtime.Composable

@Composable
expect fun rememberJsonPicker(onJsonSelected: (ByteArray?) -> Unit): () -> Unit