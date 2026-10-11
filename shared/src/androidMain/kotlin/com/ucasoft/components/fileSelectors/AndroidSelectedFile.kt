package com.ucasoft.components.fileSelectors

import android.content.Context
import android.net.Uri

class AndroidSelectedFile(
    private val context: Context,
    override val descriptor: Uri
): SelectedFile<Uri> {

    override val bytes by lazy {
        context.contentResolver.openInputStream(descriptor)?.use { inputStream ->
            inputStream.readBytes()
        } ?: ByteArray(0)
    }

    override fun toImportSource() = context.contentResolver.openFileDescriptor(descriptor, "r")
}