package com.ucasoft.modernMoney.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "currencies"
)
data class Currency(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val code: String,
    val symbol: String,
    val isVisible: Boolean = false
)
