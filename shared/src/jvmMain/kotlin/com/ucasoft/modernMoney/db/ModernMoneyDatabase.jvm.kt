package com.ucasoft.modernMoney.db

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

fun getDatabaseBuilder() : RoomDatabase.Builder<ModernMoneyDatabase> {
    val file = File(System.getProperty("java.io.tmpdir"), "UCASoft/ModernMoney/modern_money.db")
    return Room.databaseBuilder<ModernMoneyDatabase>(
        name = file.absolutePath
    )
}