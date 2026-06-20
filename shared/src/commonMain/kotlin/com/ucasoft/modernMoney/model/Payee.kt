package com.ucasoft.modernMoney.model

import androidx.compose.ui.graphics.ImageBitmap
import com.ucasoft.komm.annotations.KOMMMap
import com.ucasoft.komm.annotations.MapConfiguration
import com.ucasoft.komm.annotations.MapFunction
import com.ucasoft.modernMoney.ui.toByteArray
import com.ucasoft.modernMoney.ui.toImageBitmap
import com.ucasoft.modernMoney.db.model.Payee as DbPayee

@KOMMMap(from = [DbPayee::class], to = [DbPayee::class], context = Unit::class, config = MapConfiguration(
        allowNotNullAssertion = false,
        tryAutoCast = true,
        mapDefaultAsFallback = false,
        convertFunctionName = ""
    )
)
data class Payee(
    override val name: String,
    @MapFunction("com.ucasoft.modernMoney.ui", "")
    override val logo: ImageBitmap? = null,
    val aliases: List<String> = emptyList(),
): KeyEntity<Long>, LogoEntity {

    var id: Long = 0L
        internal set

    override val key: Long
        get() = id
}
