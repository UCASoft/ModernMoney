package com.ucasoft.modernMoney.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "currencies"
)
data class Currency(
    @PrimaryKey
    val code: String = "",
    val name: String,
    val symbol: String,
    val isVisible: Boolean = false
)
