package com.ucasoft.modernMoney.model

data class Account(
    val name: String,
    val currencies: List<AccountCurrency>,
    val cards: List<String>? = null
)

data class AccountCurrency (
    val currency: Currency,
    val balance: Double
)

data class Currency(
    val name: String,
    val symbol: String
)

data class Transaction(
    val dataTime: Int,
    val incomingAmount: Double?,
    val incomingAccountCurrency: AccountCurrency?,
    val expenseAmount: Double?,
    val expenseAccountCurrency: AccountCurrency?
)