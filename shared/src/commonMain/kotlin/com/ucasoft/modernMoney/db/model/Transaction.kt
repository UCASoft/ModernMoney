package com.ucasoft.modernMoney.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = AccountCurrency::class,
            parentColumns = [ "id" ],
            childColumns = [ "expenseCurrencyId" ],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = AccountCurrency::class,
            parentColumns = [ "id" ],
            childColumns = [ "incomeCurrencyId" ],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Payee::class,
            parentColumns = [ "id" ],
            childColumns = [ "payeeId" ],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = [ "id" ],
            childColumns = [ "categoryId" ],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Location::class,
            parentColumns = [ "id" ],
            childColumns = [ "locationId" ],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val dataTime: Instant,
    val expenseCurrencyId: Long? = null,
    val expenseAmount: Double? = null,
    val incomeCurrencyId: Long? = null,
    val incomeAmount: Double? = null,
    val payeeId: Long? = null,
    val payeeCurrencyCode: String? = null,
    val payeeAmount: Double? = null,
    val categoryId: Long? = null,
    val locationId: Long? = null,
    val comment: String? = null
)

/*data class SubTransaction(
    val id: Long = 0L,
    val parentId: Long,
    val amount: Double,
    val categoryId: Long? = null,
)*/