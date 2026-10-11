package com.ucasoft.components.fileSelectors

import com.ucasoft.modernMoney.plugin.interfaces.imports.ImportSource

interface SelectedFile<out T> {
    val descriptor: T
    val bytes: ByteArray

    fun toImportSource(): ImportSource?
}