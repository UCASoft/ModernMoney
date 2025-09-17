package com.ucasoft.modernMoney.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(context: Context) : RoomDatabase.Builder<ModernMoneyDatabase> {
    val appContext = context.applicationContext
    val file = appContext.getDatabasePath("moder_money.db")
    return Room.databaseBuilder<ModernMoneyDatabase>(
        context = appContext,
        name = file.absolutePath
    )
}