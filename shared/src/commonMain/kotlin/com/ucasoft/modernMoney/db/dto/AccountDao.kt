package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ucasoft.modernMoney.db.model.Account
import com.ucasoft.modernMoney.db.model.FullAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Transaction
    @Query("SELECT * FROM accounts ORDER BY `order`")
    fun allAccounts() : Flow<List<FullAccount>>

    @Transaction
    @Query("SELECT * FROM accounts WHERE id = :id")
    fun accountById(id: Long): Flow<FullAccount>

    @Query("""
        UPDATE accounts
        SET `order` = CASE
            WHEN `order` = :from THEN :to
            WHEN `order` = :to THEN :from
            ELSE `order`
        END
        WHERE `order` IN (:from, :to)
    """)
    suspend fun reorderItems(from: Int, to: Int)

    @Insert
    suspend fun insert(account: Account): Long

    @Update
    suspend fun update(account: Account)

    @Delete
    suspend fun delete(account: Account): Int
}