package com.ucasoft.components.fileSelectors

import java.io.File

class JvmSelectedFile(
    override val descriptor: File
): SelectedFile<File> {
    override val bytes by lazy {
        descriptor.readBytes()
    }
}