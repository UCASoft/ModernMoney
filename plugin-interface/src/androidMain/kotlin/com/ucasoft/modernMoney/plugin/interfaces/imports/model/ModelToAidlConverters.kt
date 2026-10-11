package com.ucasoft.modernMoney.plugin.interfaces.imports.model

import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportAccount as AidlImportAccount
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportAccountAmount as AidlImportAccountAmount
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportAccountCurrency as AidlImportAccountCurrency
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportBank as AidlImportBank
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportCard as AidlImportCard
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportCategory as AidlImportCategory
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportLocation as AidlImportLocation
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportPayee as AidlImportPayee
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportPayeeAmount as AidlImportPayeeAmount
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportPluginData as AidlImportPluginData
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportTransaction as AidlImportTransaction

fun ImportAccount.toAidl() = AidlImportAccount().apply {
    id = this@toAidl.id
    name = this@toAidl.name
    bankId = this@toAidl.bankId
    order = this@toAidl.order
}

fun ImportAccountAmount.toAidl() = AidlImportAccountAmount().apply {
    accountCurrencyId = this@toAidl.accountCurrencyId
    value = this@toAidl.value
}

fun ImportAccountCurrency.toAidl() = AidlImportAccountCurrency().apply {
    id = this@toAidl.id
    accountId = this@toAidl.accountId
    currencyCode = this@toAidl.currencyCode
}

fun ImportBank.toAidl() = AidlImportBank().apply {
    id = this@toAidl.id
    name = this@toAidl.name
}

fun ImportCard.toAidl() = AidlImportCard().apply {
    id = this@toAidl.id
    accountId = this@toAidl.accountId
    type = this@toAidl.type
    number = this@toAidl.number
}

fun ImportCategory.toAidl() = AidlImportCategory().apply {
    id = this@toAidl.id
    name = this@toAidl.name
    parentId = this@toAidl.parentId
    logo = this@toAidl.logo
}

fun ImportPayee.toAidl() = AidlImportPayee().apply {
    id = this@toAidl.id
    name = this@toAidl.name
    aliases = this@toAidl.aliases
}

fun ImportPayeeAmount.toAidl() = AidlImportPayeeAmount().apply {
    payeeCurrencyCode = this@toAidl.payeeCurrencyCode
    payeeAmount = this@toAidl.payeeAmount
}

fun ImportLocation.toAidl() = AidlImportLocation().apply {
    id = this@toAidl.id
    name = this@toAidl.name
    latitude = this@toAidl.latitude
    longitude = this@toAidl.longitude
    address = this@toAidl.address
}

fun ImportTransaction.toAidl() = AidlImportTransaction().apply {
    date = this@toAidl.date
    expenseAmount = this@toAidl.expenseAmount?.toAidl()
    incomeAmount = this@toAidl.incomeAmount?.toAidl()
    payeeId = this@toAidl.payeeId
    payeeAmount = this@toAidl.payeeAmount?.toAidl()
    categoryId = this@toAidl.categoryId
    comment = this@toAidl.comment
}

fun ImportPluginData.toAidl() = AidlImportPluginData().apply {
    sequenceNumber = 0
    banks = this@toAidl.banks.map { it.toAidl() }
    accounts = this@toAidl.accounts.map { it.toAidl() }
    accountCurrencies = this@toAidl.accountCurrencies.map { it.toAidl() }
    cards = this@toAidl.cards.map { it.toAidl() }
    categories = this@toAidl.categories.map { it.toAidl() }
    payees = this@toAidl.payees.map { it.toAidl() }
    transactions = this@toAidl.transactions.map { it.toAidl() }
}

fun AidlImportAccount.toModel() = ImportAccount(
    id = this.id,
    name = this.name,
    bankId = this.bankId,
    order = this.order
)

fun AidlImportAccountAmount.toModel() = ImportAccountAmount(
    accountCurrencyId = this.accountCurrencyId,
    value = this.value
)

fun AidlImportAccountCurrency.toModel() = ImportAccountCurrency(
    id = this.id,
    accountId = this.accountId,
    currencyCode = this.currencyCode
)

fun AidlImportBank.toModel() = ImportBank(
    id = this.id,
    name = this.name
)

fun AidlImportCard.toModel() = ImportCard(
    id = this.id,
    accountId = this.accountId,
    type = this.type,
    number = this.number
)

fun AidlImportCategory.toModel() = ImportCategory(
    id = this.id,
    name = this.name,
    parentId = this.parentId,
    logo = this.logo
)

fun AidlImportPayee.toModel() = ImportPayee(
    id = this.id,
    name = this.name,
    aliases = this.aliases
)

fun AidlImportPayeeAmount.toModel() = ImportPayeeAmount(
    payeeCurrencyCode = this.payeeCurrencyCode,
    payeeAmount = this.payeeAmount
)

fun AidlImportLocation.toModel() = ImportLocation(
    id = this.id,
    name = this.name,
    latitude = this.latitude,
    longitude = this.longitude,
    address = this.address
)

fun AidlImportTransaction.toModel() = ImportTransaction(
    date = this.date,
    expenseAmount = this.expenseAmount?.toModel(),
    incomeAmount = this.incomeAmount?.toModel(),
    payeeId = this.payeeId,
    payeeAmount = this.payeeAmount?.toModel(),
    categoryId = this.categoryId,
    comment = this.comment
)

fun AidlImportPluginData.toModel() = ImportPluginData(
    banks = this.banks.map { it.toModel() },
    accounts = this.accounts.map { it.toModel() },
    accountCurrencies = this.accountCurrencies.map { it.toModel() },
    cards = this.cards.map { it.toModel() },
    categories = this.categories.map { it.toModel() },
    payees = this.payees.map { it.toModel() },
    transactions = this.transactions.map { it.toModel() }
)