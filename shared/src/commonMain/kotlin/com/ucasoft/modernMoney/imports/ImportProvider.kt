package com.ucasoft.modernMoney.imports

import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

interface ImportProvider<T: ProgressStatus<*>>: KoinComponent {
    val name: String
    val description: String
    fun runImport(bytes: ByteArray?): Flow<ImportStatus<T>>
}
