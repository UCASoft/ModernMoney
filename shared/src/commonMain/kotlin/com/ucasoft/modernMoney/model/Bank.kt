package com.ucasoft.modernMoney.model

import com.ucasoft.modernMoney.db.model.Bank as DbBank

data class Bank(
    val name: String
) : KeyEntity<Long> {
    var id: Long = 0L
        internal set

    override val key = id

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