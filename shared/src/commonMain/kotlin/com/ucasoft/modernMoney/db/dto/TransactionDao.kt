package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ucasoft.modernMoney.db.filters.TransactionFilter
import com.ucasoft.modernMoney.db.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY dateTime DESC")
    fun allTransaction(): Flow<List<Transaction>>

    fun transactionsByFilter(filter: TransactionFilter) =
        transactionsByFilter(
            filter.accountId,
            filter.onlyAccountCurrencies,
            filter.currencyCode,
            filter.categoryId,
            filter.includeChildren
        )

    @Query(
        """
    WITH RECURSIVE selected_categories(id) AS (
        SELECT id
        FROM categories
        WHERE id = :categoryId
        UNION
        SELECT c.id
        FROM categories c
        INNER JOIN selected_categories sc ON c.parentId = sc.id
    )
    SELECT t.*
    FROM transactions t
    LEFT JOIN account_currencies expenseCurrency
        ON expenseCurrency.id = t.expenseCurrencyId
    LEFT JOIN account_currencies incomeCurrency
        ON incomeCurrency.id = t.incomeCurrencyId
    WHERE
        (
            :accountId IS NULL
            OR expenseCurrency.accountId = :accountId
            OR incomeCurrency.accountId = :accountId
        )
        AND (
            :currencyCode IS NULL
            OR (expenseCurrency.currencyCode = :currencyCode AND (:onlyAccountCurrencies = 0 OR expenseCurrency.accountId = :accountId))
            OR (incomeCurrency.currencyCode = :currencyCode AND (:onlyAccountCurrencies = 0 OR incomeCurrency.accountId = :accountId))
            OR (t.payeeCurrencyCode = :currencyCode AND (:onlyAccountCurrencies = 0 OR t.payeeId = :accountId))
        )
        /*AND (
            :fromDateTime IS NULL
            OR t.dateTime >= :fromDateTime
        )
        AND (
            :toDateTime IS NULL
            OR t.dateTime <= :toDateTime
        )*/
        AND (
            :categoryId IS NULL
            OR t.categoryId = :categoryId
            OR (
                :includeChildren = 1
                AND t.categoryId IN (SELECT id FROM selected_categories)
            )
        )
        /*AND (
            :payeeId IS NULL
            OR t.payeeId = :payeeId
        )
        AND (
            :comment IS NULL
            OR t.comment LIKE '%' || :comment || '%'
        )*/
    ORDER BY t.dateTime DESC
    """
    )
    fun transactionsByFilter(
        accountId: Long?,
        onlyAccountCurrencies: Boolean,
        currencyCode: String?,
        categoryId: Long?,
        includeChildren: Boolean
    ): Flow<List<Transaction>>

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
