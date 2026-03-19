package com.ucasoft.modernMoney.db.converters

import androidx.room.TypeConverter

class ListStringConverter {
    @TypeConverter
    fun fromListString(value: List<String>?) = value?.joinToString("$#$")

    @TypeConverter
    fun toListString(value: String?) = value?.split("$#$")?.map { it.trim() }?.filterNot { it.isBlank() } ?: emptyList()

}