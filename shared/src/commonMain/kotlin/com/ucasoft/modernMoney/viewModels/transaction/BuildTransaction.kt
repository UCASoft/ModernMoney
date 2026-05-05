package com.ucasoft.modernMoney.viewModels.transaction

import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.model.Category

data class BuildTransaction (
    val expenseAccountCurrency: AccountCurrency? = null,
    val incomeAccountCurrency: AccountCurrency? = null,
    val expenseAccount: Account? = null,
    val incomeAccount: Account? = null,
    val category: Category? = null
)