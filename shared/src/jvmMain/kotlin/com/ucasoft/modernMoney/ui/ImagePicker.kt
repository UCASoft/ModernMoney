package com.ucasoft.modernMoney.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun rememberImagePicker(onImageSelected: (ByteArray?) -> Unit): () -> Unit {
    return remember {
        {
            val dialog = FileDialog(null as Frame?, "Select Logo", FileDialog.LOAD)
            dialog.file = "*.png"
            dialog.isVisible = true
            val file = dialog.file
            val dir = dialog.directory
            if (file != null) {
                onImageSelected(File(dir, file).readBytes())
            } else {
                onImageSelected(null)
            }
        }
    }
}
