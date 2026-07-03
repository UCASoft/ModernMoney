package com.ucasoft.modernMoney.imports.money.model

import com.ucasoft.komm.abstractions.KOMMConverter
import com.ucasoft.komm.annotations.KOMMMap
import com.ucasoft.komm.annotations.MapConfiguration
import com.ucasoft.komm.annotations.MapConvert
import com.ucasoft.modernMoney.model.AccountCard
import kotlinx.serialization.Serializable

@Serializable
@KOMMMap(
    from = [],
    to = [AccountCard::class],
    context = Unit::class,
    config = MapConfiguration(
        allowNotNullAssertion = false,
        tryAutoCast = true,
        mapDefaultAsFallback = false,
        nullableContext = false,
        convertFunctionName = ""
    )
)
data class Card(
    val id: Long,
    val accountId: Long,
    val number: String,
    @MapConvert<Card, AccountCard, TypeConverter>(TypeConverter::class, "type")
    val logoResource: String
)

class TypeConverter(source: Card) : KOMMConverter<Card, String, AccountCard, String>(source) {
    override fun convert(sourceMember: String) = when(val oldType = sourceMember.substringAfter("ic_").substringBefore("_")) {
        "master" -> "mastercard"
        else -> oldType
    }
}