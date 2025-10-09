package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ucasoft.modernMoney.db.model.Bank
import kotlinx.coroutines.flow.Flow

@Dao
interface BankDao {

    @Query("SELECT * FROM banks")
    fun allBanks() : Flow<List<Bank>>

    @Query("SELECT * FROM banks WHERE id = :id")
    fun bankById(id: Long) : Flow<Bank>

    @Insert
    suspend fun insert(bank: Bank): Long

    @Update
    suspend fun update(bank: Bank)

    @Delete
    suspend fun delete(bank: Bank)
}