package com.ucasoft.components.fileSelectors

interface SelectedFile<out T> {
    val descriptor: T
    val bytes: ByteArray
}