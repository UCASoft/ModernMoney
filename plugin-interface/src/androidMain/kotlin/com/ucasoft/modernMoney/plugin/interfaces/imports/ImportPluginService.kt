package com.ucasoft.modernMoney.plugin.interfaces.imports

import android.app.Service
import android.content.Intent
import android.os.Parcel
import android.os.ParcelFileDescriptor
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.IImportPluginCallback
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.IImportPluginInterface
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportPluginData
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.toAidl

abstract class ImportPluginService: Service() {

    private val binder = object: IImportPluginInterface.Stub() {
        override fun startImport(
            source: ParcelFileDescriptor?,
            callback: IImportPluginCallback
        ) {
            ParcelFileDescriptor.AutoCloseInputStream(source).use {
                val importData = parse(it.readBytes())
                val processingSize = importData.size
                var processedSize = 0
                var chunk = prepareNextChunk(importData, processedSize)
                while(chunk.size > 0) {
                    callback.onDataChunk(chunk.toAidl())
                    processedSize += chunk.size
                    callback.onProgress("Importing", "Processing chunks...", processedSize * 100 / processingSize)
                    chunk = prepareNextChunk(importData, processedSize)
                }
                callback.onCompleted()
            }
        }

        override fun cancelImport() {}
    }

    private fun prepareNextChunk(importData: ImportPluginData, processedSize: Int): ImportPluginData {
        if (processedSize == importData.size) {
            return ImportPluginData()
        }

        val remaining = importData.size - processedSize
        var low = 1
        var high = remaining
        var bestCount = 0

        while (low <= high) {
            val count = low + (high - low) / 2
            if (parcelSize(makeChunk(importData, processedSize, count)) <= MAX_CHUNK_BYTES) {
                bestCount = count
                low = count + 1
            } else {
                high = count - 1
            }
        }

        return makeChunk(importData, processedSize, bestCount)
    }

    private fun parcelSize(data: ImportPluginData): Int {
        val parcel = Parcel.obtain()
        return try {
            parcel.writeParcelable(data.toAidl(), 0)
            parcel.dataSize()
        } finally {
            parcel.recycle()
        }
    }

    private fun makeChunk(importData: ImportPluginData, processedSize: Int, count: Int) = ImportPluginData(
        banks = slice(importData.banks, processedSize, 0, count),
        accounts = slice(importData.accounts, processedSize, importData.banks.size, count),
        accountCurrencies = slice(
            importData.accountCurrencies,
            processedSize,
            importData.banks.size + importData.accounts.size,
            count
        ),
        cards = slice(
            importData.cards,
            processedSize,
            importData.banks.size + importData.accounts.size +
                    importData.accountCurrencies.size,
            count
        ),
        categories = slice(
            importData.categories,
            processedSize,
            importData.banks.size + importData.accounts.size +
                    importData.accountCurrencies.size + importData.cards.size,
            count
        ),
        payees = slice(
            importData.payees,
            processedSize,
            importData.banks.size + importData.accounts.size +
                    importData.accountCurrencies.size + importData.cards.size +
                    importData.categories.size,
            count
        ),
        locations = slice(
            importData.locations,
            processedSize,
            importData.banks.size + importData.accounts.size +
                    importData.accountCurrencies.size + importData.cards.size +
                    importData.categories.size + importData.payees.size,
            count
        ),
        transactions = slice(
            importData.transactions,
            processedSize,
            importData.banks.size + importData.accounts.size +
                    importData.accountCurrencies.size + importData.cards.size +
                    importData.categories.size + importData.payees.size + importData.locations.size,
            count
        )
    )

    private fun <T> slice(source: List<T>, processedSize: Int, offset: Int, count: Int): List<T> {
        val from = (processedSize - offset).coerceAtLeast(0)
        val to = (processedSize + count - offset).coerceAtMost(source.size)
        return if (from >= to) emptyList() else source.subList(from, to).toList()
    }

    override fun onBind(intent: Intent?) = binder

    abstract fun parse(data: ByteArray): ImportPluginData

    private companion object {
        private const val MAX_CHUNK_BYTES = 512 * 1024
    }
}