package com.ucasoft.modernMoney.plugin.interfaces.imports.model

data class ImportTransaction(
    val date: Long,
    val expenseAmount: ImportAccountAmount?,
    val incomeAmount: ImportAccountAmount?,
    val payeeId: String?,
    val payeeAmount: ImportPayeeAmount?,
    val categoryId: String?,
    val comment: String?
)