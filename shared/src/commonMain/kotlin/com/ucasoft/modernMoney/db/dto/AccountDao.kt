package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ucasoft.modernMoney.db.model.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM accounts")
    fun allAccounts() : Flow<List<Account>>

    @Insert
    suspend fun insert(account: Account): Long

    @Delete
    suspend fun delete(account: Account): Int
}