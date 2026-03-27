package com.ucasoft.modernMoney.db.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "accounts",
    foreignKeys = [
        ForeignKey(
            entity = Bank::class,
            parentColumns = [ "id" ],
            childColumns = [ "bankId" ],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val bankId: Long? = null,
    val order: Int = 0
)

data class AccountWithCurrencies(
    @Embedded
    val account: Account,
    @Relation(
        parentColumn = "id",
        entityColumn = "accountId",
        entity = AccountCurrency::class
    )
    val currencies: List<AccountCurrencyWithCurrency>,
    @Relation(
        parentColumn = "bankId",
        entityColumn = "id"
    )
    val bank: Bank?,
    @Relation(
        parentColumn = "id",
        entityColumn = "accountId"
    )
    val cards: List<AccountCard>
)