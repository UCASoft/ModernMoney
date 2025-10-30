package com.ucasoft.modernMoney.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "account_currencies",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = [ "id" ],
            childColumns = [ "accountId" ],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Currency::class,
            parentColumns = [ "id" ],
            childColumns = [ "currencyId" ],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class AccountCurrency (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val accountId: Long,
    val currencyId: Long
)
