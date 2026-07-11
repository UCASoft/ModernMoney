package com.ucasoft.modernMoney.imports.money.model

import androidx.compose.ui.graphics.ImageBitmap
import com.ucasoft.komm.annotations.KOMMMap
import com.ucasoft.komm.annotations.MapConfiguration
import com.ucasoft.komm.annotations.MapDefault
import com.ucasoft.komm.annotations.MapTargetDefault
import com.ucasoft.komm.plugins.iterable.annotations.KOMMIterableString
import com.ucasoft.modernMoney.model.Payee as MMPayee
import kotlinx.serialization.Serializable

@Serializable
@KOMMMap(
    from = [],
    to = [MMPayee::class],
    context = Unit::class,
    config = MapConfiguration(
        allowNotNullAssertion = false,
        tryAutoCast = true,
        mapDefaultAsFallback = false,
        nullableContext = false,
        convertFunctionName = ""
    )
)
@MapTargetDefault(
    "logo",
    MapDefault(PayeeNullableResolver::class)
)
data class Payee(
    val id: Long,
    val name: String,
    @KOMMIterableString(";")
    val aliases: String
)

class PayeeNullableResolver(destination: Payee?) : NullableResolver<Payee, ImageBitmap?>(destination)