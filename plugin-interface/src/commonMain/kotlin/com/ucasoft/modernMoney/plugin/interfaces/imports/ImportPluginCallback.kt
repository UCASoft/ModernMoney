package com.ucasoft.modernMoney.plugin.interfaces.imports

import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportPluginData

interface ImportPluginCallback {
    fun onProgress(phase: String?, message: String?, percent: Int)
    fun onDataChunk(chunk: ImportPluginData)
    fun onCompleted()
    fun onError(message: String)
}