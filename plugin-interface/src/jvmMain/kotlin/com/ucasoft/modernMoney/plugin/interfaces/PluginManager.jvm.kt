package com.ucasoft.modernMoney.plugin.interfaces

import com.ucasoft.modernMoney.plugin.interfaces.imports.ImportPluginCallback
import com.ucasoft.modernMoney.plugin.interfaces.imports.ImportSource

actual class PluginManager {
    actual fun loadPlugins(): List<PluginInfo> = emptyList()

    actual fun initImportPlugin(pluginInfo: PluginInfo, source: ImportSource?, callback: ImportPluginCallback) {
    }
}
