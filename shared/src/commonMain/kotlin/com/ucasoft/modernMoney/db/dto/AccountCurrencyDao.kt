package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ucasoft.modernMoney.db.model.AccountCurrency
import com.ucasoft.modernMoney.db.model.AccountCurrencyWithCurrency
import com.ucasoft.modernMoney.db.model.CurrencyBalance
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountCurrencyDao {
    @Query("SELECT * FROM account_currencies")
    fun accountCurrencies() : Flow<List<AccountCurrencyWithCurrency>>

    @Query("SELECT * FROM account_currencies WHERE accountId = :accountId")
    fun accountCurrencies(accountId: Long) : Flow<List<AccountCurrency>>

    @Query("SELECT DISTINCT currencyCode FROM account_currencies")
    fun currencyCodes(): Flow<List<String>>

    @Query("""
        SELECT currencyId, ROUND(SUM(amount), 2) + 0.0 AS balance FROM (
            SELECT incomeCurrencyId AS currencyId, incomeAmount AS amount
            FROM transactions WHERE incomeCurrencyId IS NOT NULL
            UNION ALL
            SELECT expenseCurrencyId AS currencyId, -expenseAmount AS amount
            FROM transactions WHERE expenseCurrencyId IS NOT NULL
        )
        GROUP BY currencyId
    """)
    fun currencyBalances(): Flow<List<CurrencyBalance>>

    @Insert
    suspend fun insert(currency: AccountCurrency)
}