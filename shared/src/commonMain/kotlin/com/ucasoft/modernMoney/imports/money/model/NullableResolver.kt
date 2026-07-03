package com.ucasoft.modernMoney.imports.money.model

import com.ucasoft.komm.abstractions.KOMMResolver

open class NullableResolver<T, P>(destination: T?) : KOMMResolver<T, P?>(destination) {
    override fun resolve() = null
}