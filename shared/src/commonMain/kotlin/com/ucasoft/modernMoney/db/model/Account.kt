package com.ucasoft.modernMoney.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val currency: String
)