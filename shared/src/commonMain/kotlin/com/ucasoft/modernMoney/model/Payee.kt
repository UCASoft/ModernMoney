package com.ucasoft.modernMoney.model

import androidx.compose.ui.graphics.ImageBitmap
import com.ucasoft.modernMoney.ui.toByteArray
import com.ucasoft.modernMoney.ui.toImageBitmap
import com.ucasoft.modernMoney.db.model.Payee as DbPayee

data class Payee(
    override val name: String,
    override val logo: ImageBitmap? = null,
    val aliases: List<String> = emptyList(),
): KeyEntity<Long>, LogoEntity {

    var id: Long = 0L
        internal set

    override val key: Long
        get() = id

    fun mapToPayee() =
        DbPayee(
            id = id,
            name = name,
            logo = logo?.toByteArray(),
            aliases = aliases
        )
}

fun DbPayee.mapToPayee() =
    Payee(
        name,
        logo?.toImageBitmap(),
        aliases
    ).also { it.id = id }
