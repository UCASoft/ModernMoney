package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Insert
import com.ucasoft.modernMoney.db.model.Bank

@Dao
interface BankDao {

    @Insert
    suspend fun insert(bank: Bank): Long
}