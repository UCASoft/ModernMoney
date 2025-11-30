package com.ucasoft.modernMoney.model

import com.ucasoft.modernMoney.db.model.AccountCard as DbAccountCard

data class AccountCard(
    val type: String,
    val number: String
) {
    var id: Long = 0L
        internal set

    fun mapToDbAccountCard(accountId: Long) =
        DbAccountCard(id, accountId, type, number)
}

fun DbAccountCard.mapToAccountCard() =
    AccountCard(
        type = type,
        number = number
    ).also { it.id = id }