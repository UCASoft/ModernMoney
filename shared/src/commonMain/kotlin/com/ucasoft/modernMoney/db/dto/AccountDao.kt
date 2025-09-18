package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ucasoft.modernMoney.db.model.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM account")
    fun allAccounts() : Flow<List<Account>>

    @Insert
    suspend fun insert(account: Account)
}