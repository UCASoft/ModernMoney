package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ucasoft.modernMoney.db.model.CurrencyExchange
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyExchangeDao {

    @Query("SELECT * FROM currency_exchanges")
    fun allExchanges(): Flow<List<CurrencyExchange>>

    suspend fun refreshExchanges(exchanges: List<CurrencyExchange>) {
        deleteAll()
        insert(exchanges)
    }

    @Insert
    suspend fun insert(currencyExchanges: List<CurrencyExchange>)

    @Query("DELETE FROM currency_exchanges")
    suspend fun deleteAll()
}