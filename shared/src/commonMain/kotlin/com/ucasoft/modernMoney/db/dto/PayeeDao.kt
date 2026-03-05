package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ucasoft.modernMoney.db.model.Payee
import kotlinx.coroutines.flow.Flow

@Dao
interface PayeeDao {

    @Query("SELECT * FROM payees")
    fun allPayees() : Flow<List<Payee>>

    @Query("SELECT * FROM payees WHERE id = :id")
    fun payeeById(id: Long) : Flow<Payee>

    @Insert
    suspend fun insert(payee: Payee): Long

    @Update
    suspend fun update(payee: Payee)

    @Delete
    suspend fun delete(payee: Payee)
}