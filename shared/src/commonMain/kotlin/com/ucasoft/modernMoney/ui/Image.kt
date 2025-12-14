package com.ucasoft.modernMoney.ui

import androidx.compose.ui.graphics.ImageBitmap

expect fun ByteArray.toImageBitmap(): ImageBitmap

expect fun ImageBitmap.toByteArray(): ByteArray
