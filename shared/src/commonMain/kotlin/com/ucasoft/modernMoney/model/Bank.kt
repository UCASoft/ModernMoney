package com.ucasoft.modernMoney.model

import androidx.compose.ui.graphics.ImageBitmap
import com.ucasoft.modernMoney.db.model.Bank as DbBank
import com.ucasoft.modernMoney.ui.toImageBitmap
import com.ucasoft.modernMoney.ui.toByteArray

data class Bank(
    val name: String,
    override val logo: ImageBitmap? = null
) : KeyEntity<Long>, LogoEntity {
    var id: Long = 0L
        internal set

    override val key: Long
        get() = id

    fun mapToBank() =
        DbBank(
            id = id,
            name = name,
            logo = logo?.toByteArray()
        )
}

fun DbBank.mapToBank() =
    Bank(
        name,
        logo?.toImageBitmap()
    ).also { it.id = id }