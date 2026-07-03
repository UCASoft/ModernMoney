package com.ucasoft.modernMoney.imports.money.model

import androidx.compose.ui.graphics.ImageBitmap
import com.ucasoft.komm.annotations.KOMMMap
import com.ucasoft.komm.annotations.MapConfiguration
import com.ucasoft.komm.annotations.MapDefault
import com.ucasoft.komm.annotations.MapTargetDefault
import com.ucasoft.modernMoney.model.Bank as MMBank
import kotlinx.serialization.Serializable
import javax.print.attribute.standard.Destination

@Serializable
@KOMMMap(
    from = [],
    to = [MMBank::class],
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
    MapDefault(BankNullableResolver::class)
)
data class Bank(
    val id: Long,
    val name: String
)

class BankNullableResolver(destination: Bank?) : NullableResolver<Bank, ImageBitmap?>(destination)
