package com.ucasoft.modernMoney.db.model

import androidx.room.Entity

@Entity(
    tableName = "currency_exchanges",
    primaryKeys = [ "from", "to" ]
)
data class CurrencyExchange(
    val from: String,
    val to: String,
    val amount: Double
)
