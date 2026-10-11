package com.ucasoft.modernMoney.model

import androidx.compose.ui.graphics.ImageBitmap
import com.ucasoft.komm.annotations.KOMMMap
import com.ucasoft.komm.annotations.MapConfiguration
import com.ucasoft.komm.annotations.MapDefault
import com.ucasoft.komm.annotations.MapFunction
import com.ucasoft.komm.plugins.iterable.annotations.KOMMIterableString
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.ImportPayee
import com.ucasoft.modernMoney.db.model.Payee as DbPayee

@KOMMMap(
    from = [DbPayee::class, ImportPayee::class],
    to = [DbPayee::class],
    context = Unit::class,
    config = MapConfiguration(
        allowNotNullAssertion = false,
        tryAutoCast = true,
        mapDefaultAsFallback = false,
        nullableContext = false,
        convertFunctionName = ""
    )
)
data class Payee(
    override val name: String,
    @MapFunction("com.ucasoft.modernMoney.ui", "", `for` = [DbPayee::class])
    @MapDefault<PayeeNullableResolver>(PayeeNullableResolver::class, `for` = [ImportPayee::class])
    override val logo: ImageBitmap? = null,
    @KOMMIterableString(";")
    val aliases: List<String> = emptyList(),
): KeyEntity<Long>, LogoEntity {

    var id: Long = 0L
        internal set

    override val key: Long
        get() = id
}

class PayeeNullableResolver(destination: Payee?) : NullableResolver<Payee, ImageBitmap?>(destination)