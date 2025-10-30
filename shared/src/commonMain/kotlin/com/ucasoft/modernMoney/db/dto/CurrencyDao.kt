package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Insert
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
}