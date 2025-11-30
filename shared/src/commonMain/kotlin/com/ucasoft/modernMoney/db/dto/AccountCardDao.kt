package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ucasoft.modernMoney.db.model.AccountCard

@Dao
interface AccountCardDao {

    @Query("SELECT * FROM account_cards WHERE accountId = :accountId")
    suspend fun accountCards(accountId: Long) : List<AccountCard>

    @Insert
    suspend fun insert(card: AccountCard)

    @Insert
    suspend fun insert(card: List<AccountCard>)

    suspend fun refreshCards(accountId: Long, cards: List<AccountCard>) {
        removeOld(accountId, cards.map { it.id })
        insert(cards)
    }

    @Query("""
        DELETE FROM account_cards
        WHERE accountId = :accountId
        AND id NOT IN (:cardIds)
    """)
    suspend fun removeOld(accountId: Long, cardIds: List<Long>)
}