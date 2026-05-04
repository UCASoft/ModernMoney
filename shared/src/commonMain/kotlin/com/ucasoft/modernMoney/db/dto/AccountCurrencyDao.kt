package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ucasoft.modernMoney.db.model.AccountCurrency
import com.ucasoft.modernMoney.db.model.AccountCurrencyWithCurrency
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountCurrencyDao {
    @Query("SELECT * FROM account_currencies")
    fun accountCurrencies() : Flow<List<AccountCurrencyWithCurrency>>

    @Query("SELECT * FROM account_currencies WHERE accountId = :accountId")
    suspend fun accountCurrencies(accountId: Long) : List<AccountCurrency>

    @Insert
    suspend fun insert(currency: AccountCurrency)
}