package com.ucasoft.modernMoney

import androidx.compose.runtime.Composable

@Composable
expect fun AppLocaleProvider(language: String?, content: @Composable () -> Unit)