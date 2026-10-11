package com.ucasoft.modernMoney.plugin.interfaces.imports.model

data class ImportPluginData(
    val banks: List<ImportBank> = emptyList(),
    val accounts: List<ImportAccount> = emptyList(),
    val accountCurrencies: List<ImportAccountCurrency> = emptyList(),
    val cards: List<ImportCard> = emptyList(),
    val categories: List<ImportCategory> = emptyList(),
    val payees: List<ImportPayee> = emptyList(),
    val locations: List<ImportLocation> = emptyList(),
    val transactions: List<ImportTransaction> = emptyList()
) {
    val size: Int
        get() = banks.size + accounts.size + accountCurrencies.size + cards.size + categories.size + payees.size + locations.size + transactions.size

    operator fun plus(other: ImportPluginData): ImportPluginData {
        return ImportPluginData(
            banks = this.banks + other.banks,
            accounts = this.accounts + other.accounts,
            accountCurrencies = this.accountCurrencies + other.accountCurrencies,
            cards = this.cards + other.cards,
            categories = this.categories + other.categories,
            payees = this.payees + other.payees,
            locations = this.locations + other.locations,
            transactions = this.transactions + other.transactions
        )
    }
}
