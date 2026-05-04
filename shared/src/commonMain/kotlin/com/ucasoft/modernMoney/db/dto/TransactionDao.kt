package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ucasoft.modernMoney.db.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    fun allTransaction(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun transactionById(id: Long): Flow<Transaction>

    @Insert
    suspend fun insert(transaction: Transaction): Long

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)
}