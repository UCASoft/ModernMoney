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
    fun accountCurrencies(accountId: Long) : Flow<List<AccountCurrency>>

    @Query("SELECT DISTINCT currencyCode FROM account_currencies")
    fun currencyCodes(): Flow<List<String>>

    @Insert
    suspend fun insert(currency: AccountCurrency)
}