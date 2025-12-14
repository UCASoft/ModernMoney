package com.ucasoft.modernMoney.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

actual fun ByteArray.toImageBitmap() =
    BitmapFactory.decodeByteArray(this, 0, this.size).asImageBitmap()

actual fun ImageBitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
