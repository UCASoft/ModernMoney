package com.ucasoft.modernMoney.ui

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

actual fun ByteArray.toImageBitmap() =
    ImageIO.read(ByteArrayInputStream(this)).toComposeImageBitmap()

actual fun ImageBitmap.toByteArray(): ByteArray {
    val bufferedImage = this.toAwtImage()
    val stream = ByteArrayOutputStream()
    ImageIO.write(bufferedImage, "png", stream)
    return stream.toByteArray()
}
