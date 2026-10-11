package com.ucasoft.modernMoney.plugin.interfaces

import com.ucasoft.modernMoney.plugin.interfaces.imports.ImportPluginCallback
import com.ucasoft.modernMoney.plugin.interfaces.imports.ImportSource

expect class PluginManager {
    fun loadPlugins(): List<PluginInfo>

    fun initImportPlugin(pluginInfo: PluginInfo, source: ImportSource?, callback: ImportPluginCallback)
}