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

    @Query("SELECT * FROM transactions ORDER BY dateTime DESC")
    fun allTransaction(): Flow<List<Transaction>>

    @Query(
        """
        WITH user_currencies AS (
            SELECT id FROM account_currencies WHERE accountId = :accountId
        )
        SELECT * FROM transactions 
        WHERE expenseCurrencyId IN user_currencies
        OR incomeCurrencyId IN user_currencies
        ORDER BY dateTime DESC;
        """
    )
    fun transactionsByAccountId(accountId: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun transactionById(id: Long): Flow<Transaction>

    @Insert
    suspend fun insert(transaction: Transaction): Long

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("DELETE FROM transactions WHERE expenseCurrencyId is NULL and incomeCurrencyId is NULL")
    suspend fun clearTransaction()
}
