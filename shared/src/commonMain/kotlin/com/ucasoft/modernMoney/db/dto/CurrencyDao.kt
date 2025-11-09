package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ucasoft.modernMoney.db.model.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencies")
    fun allCurrencies() : Flow<List<Currency>>

    @Query("SELECT * FROM currencies WHERE isVisible = true")
    fun visibleCurrencies() : Flow<List<Currency>>

    @Insert
    suspend fun insert(currency: Currency)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(currencies: List<Currency>)

    suspend fun refreshCurrencies(currencies: List<Currency>) {
        val codes = currencies.map { it.code }
        removeUnused(codes)
        hideCurrencies(codes)
        showCurrencies(codes)
        insert(currencies)
    }

    @Query("""
        DELETE FROM currencies
        WHERE code NOT IN (:codes)
        AND NOT EXISTS(SELECT 1 FROM account_currencies a WHERE a.currencyCode = code)
    """)
    suspend fun removeUnused(codes: List<String>)

    @Query("""
        UPDATE currencies
        SET isVisible = false
        WHERE code NOT IN (:codes)
    """)
    suspend fun hideCurrencies(codes: List<String>)

    @Query("""
        UPDATE currencies
        SET isVisible = true
        WHERE code IN (:codes)
    """)
    suspend fun showCurrencies(codes: List<String>)
}