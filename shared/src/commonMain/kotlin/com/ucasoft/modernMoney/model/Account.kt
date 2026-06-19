package com.ucasoft.modernMoney.model

import com.ucasoft.komm.abstractions.KOMMConverter
import com.ucasoft.komm.annotations.KOMMMap
import com.ucasoft.komm.annotations.MapConfiguration
import com.ucasoft.komm.annotations.MapConvert
import com.ucasoft.komm.annotations.MapEmbedded
import com.ucasoft.modernMoney.db.model.FullAccount
import com.ucasoft.modernMoney.db.repositories.BankRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.ucasoft.modernMoney.db.model.Account as DbAccount

@KOMMMap(from = [FullAccount::class], to = [], config = MapConfiguration(allowNotNullAssertion = false, tryAutoCast = true, mapDefaultAsFallback = false, convertFunctionName = ""))
@MapEmbedded("account")
data class Account(
    val name: String = "",
    val currencies: List<AccountCurrency> = emptyList(),
    @MapConvert<FullAccount, Account, BankResolver>(BankResolver::class, "bankId")
    val bank: Bank? = null,
    val order: Int = 0,
    val cards: List<AccountCard> = emptyList()
) : KeyEntity<Long> {
    var id: Long = 0L
        internal set

    override val key: Long
        get() = id

    val isBankAccount: Boolean
        get() = bank != null

    fun mapToDbAccount() =
        DbAccount(
            id,
            name,
            bankId = bank?.id,
            order
        )
}

class BankResolver(account: FullAccount) : KOMMConverter<FullAccount, Long?, Account, Bank?>(account), KoinComponent {

    private val bankRepository by inject<BankRepository>()

    override fun convert(sourceMember: Long?): Bank? {
        return bankRepository.banks.value[sourceMember]
    }
}
