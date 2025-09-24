package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ucasoft.modernMoney.db.model.AccountCurrency

@Dao
interface AccountCurrencyDao {
    @Query("SELECT * FROM account_currencies WHERE accountId = :accountId")
    suspend fun accountCurrency(accountId: Long) : List<AccountCurrency>

    @Insert
    suspend fun insert(currency: AccountCurrency)
}