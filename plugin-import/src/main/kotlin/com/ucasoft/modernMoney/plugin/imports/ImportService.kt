package com.ucasoft.modernMoney.plugin.imports

import com.ucasoft.modernMoney.plugin.imports.model.Backup
import com.ucasoft.modernMoney.plugin.interfaces.imports.ImportPluginService
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportPluginData
import kotlinx.serialization.json.Json

class ImportService: ImportPluginService() {

    private val parser = ImportParser()

    override fun parse(data: ByteArray) = parser.parse(data)
}