package com.ucasoft.modernMoney.plugin.imports.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

val json = Json { ignoreUnknownKeys = true }

@Serializable
internal data class Backup(val data: List<Table>){

    val recordCount = data.sumOf { it.records.size }

    inline fun <reified T> getTableRecords() : List<T> {
        val tableName = when(T::class) {
            AccountBank::class -> "account_bank"
            Category::class -> "categories"
            Currency::class -> "currencies"
            else -> "${T::class.simpleName!!.lowercase()}s"
        }
        return data.firstOrNull { it.name == tableName }?.records?.map {
            json.decodeFromJsonElement<T>(it)
        } ?: emptyList()
    }
}