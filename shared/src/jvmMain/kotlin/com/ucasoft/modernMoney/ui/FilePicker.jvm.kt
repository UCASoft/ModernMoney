package com.ucasoft.modernMoney.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun rememberFilePicker(title: String, extension: String, onFileSelected: (ByteArray?) -> Unit): () -> Unit {
    return remember {
        {
            val dialog = FileDialog(null as Frame?, title, FileDialog.LOAD)
            dialog.file = extension
            dialog.isVisible = true
            val file = dialog.file
            val dir = dialog.directory
            if (file != null) {
                onFileSelected(File(dir, file).readBytes())
            } else {
                onFileSelected(null)
            }
        }
    }
}