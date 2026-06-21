package com.ucasoft.modernMoney.model

import androidx.compose.ui.graphics.ImageBitmap
import com.ucasoft.komm.annotations.KOMMMap
import com.ucasoft.komm.annotations.MapConfiguration
import com.ucasoft.komm.annotations.MapFunction
import com.ucasoft.komm.annotations.MapName
import com.ucasoft.modernMoney.db.model.Account
import com.ucasoft.modernMoney.db.model.Bank as DbBank

@KOMMMap(
    from = [DbBank::class],
    to = [DbBank::class],
    context = Unit::class,
    config = MapConfiguration(
        allowNotNullAssertion = false,
        tryAutoCast = true,
        mapDefaultAsFallback = false,
        nullableContext = false,
        convertFunctionName = ""
    )
)
data class Bank(
    override val name: String,
    @MapFunction("com.ucasoft.modernMoney.ui", "")
    override val logo: ImageBitmap? = null
) : KeyEntity<Long>, LogoEntity {
    @MapName("bankId", `for` = [Account::class])
    var id: Long = 0L
        internal set

    override val key: Long
        get() = id
}