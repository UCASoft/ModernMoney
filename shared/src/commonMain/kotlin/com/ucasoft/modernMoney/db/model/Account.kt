package com.ucasoft.modernMoney.db.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String
)

data class AccountWithCurrencies(
    @Embedded
    val account: Account,
    @Relation(
        parentColumn = "id",
        entityColumn = "accountId"
    )
    val currencies: List<AccountCurrency>
)