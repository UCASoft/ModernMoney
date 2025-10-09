package com.ucasoft.modernMoney.model

import com.ucasoft.modernMoney.db.model.Bank as DbBank

data class Bank(
    val name: String
) {
    var id: Long = 0L
        internal set

    fun mapToBank() =
        DbBank(
            id = id,
            name = name
        )
}

fun DbBank.mapToBank() =
    Bank(
        name
    ).also { it.id = id }