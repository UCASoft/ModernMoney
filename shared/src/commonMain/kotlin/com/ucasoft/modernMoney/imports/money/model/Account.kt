package com.ucasoft.modernMoney.imports.money.model

import com.ucasoft.modernMoney.model.Account as MMAccount
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.model.Currency as MMCurrency
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: Long,
    val name: String,
    val order: Int
) {
    fun toModernMoney(backupCurrencies: List<Currency>, currencies: Set<MMCurrency>, banks: List<Bank>, accountBank: List<AccountBank>, cards: List<Card>): MMAccount {

        val bank = banks.firstOrNull { it.id == accountBank.firstOrNull { a -> a.accountId == id }?.bankId }
        return MMAccount(
            name = name,
            currencies = backupCurrencies.filter { it.accountId == id }.map { currency ->
                AccountCurrency(id, currencies.first { it.code == currency.currencyCode.trim() })
            },
            bank = bank,
            cards = cards.filter { it.accountId == id }.map { it.toAccountCard() },
            order = order
        ).also {
            it.id = id
        }
    }
}