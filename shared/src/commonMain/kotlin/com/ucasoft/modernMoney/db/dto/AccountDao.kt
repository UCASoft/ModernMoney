package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.ucasoft.modernMoney.db.model.Account
import com.ucasoft.modernMoney.db.model.AccountWithCurrencies
import com.ucasoft.modernMoney.model.AccountCurrency
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Transaction
    @Query("SELECT * FROM accounts")
    fun allAccounts() : Flow<List<AccountWithCurrencies>>

    @Insert
    suspend fun insert(account: Account): Long

    @Delete
    suspend fun delete(account: Account): Int
}