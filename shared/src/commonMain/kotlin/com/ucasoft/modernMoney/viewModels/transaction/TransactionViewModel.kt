package com.ucasoft.modernMoney.viewModels.transaction

import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.TransactionDao
import com.ucasoft.modernMoney.db.repositories.AccountCurrencyRepository
import com.ucasoft.modernMoney.db.repositories.AccountRepository
import com.ucasoft.modernMoney.db.repositories.CategoryRepository
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.model.Category
import com.ucasoft.modernMoney.model.Transaction
import com.ucasoft.modernMoney.model.mapToTransaction
import com.ucasoft.modernMoney.viewModels.DetailViewModel
import com.ucasoft.modernMoney.viewModels.DetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Instant

class TransactionViewModel(
    private val transactionDao: TransactionDao,
    accountCurrencyRepository: AccountCurrencyRepository,
    accountRepository: AccountRepository,
    categoryRepository: CategoryRepository,
    id: Long?
) : DetailViewModel<Transaction, TransactionUiState>() {

    override val state: StateFlow<TransactionUiState>
        field = MutableStateFlow(TransactionUiState(isLoading = true))

    init {
        if (id != null) {
            viewModelScope.launch {
                transactionDao.transactionById(id)
                    .combine(accountCurrencyRepository.accountCurrencies) { transaction, accountCurrencies ->
                        transaction to BuildTransaction(
                            transaction.expenseCurrencyId?.let { accountCurrencies[it] },
                            transaction.incomeCurrencyId?.let { accountCurrencies[it] }
                        )
                    }
                    .combine(accountRepository.accounts) { transactionWithBuild, accounts ->
                        transactionWithBuild.first to transactionWithBuild.second.copy(
                            expenseAccount = transactionWithBuild.second.expenseAccountCurrency?.let { accounts[it.accountId] },
                            incomeAccount = transactionWithBuild.second.incomeAccountCurrency?.let { accounts[it.accountId] }
                        )
                    }
                    .combine(categoryRepository.categories) { transactionWithBuild, categories ->
                        transactionWithBuild.first to transactionWithBuild.second.copy(
                            category = transactionWithBuild.first.categoryId?.let { categories[it] }
                        )
                    }
                    .collect { transactionWithBuild ->
                        state.update {
                            it.copy(
                                entity = transactionWithBuild.first.mapToTransaction(
                                    expenseAccount = transactionWithBuild.second.expenseAccount,
                                    expenseAccountCurrency = transactionWithBuild.second.expenseAccountCurrency,
                                    incomeAccount = transactionWithBuild.second.incomeAccount,
                                    incomeAccountCurrency = transactionWithBuild.second.incomeAccountCurrency,
                                    category = transactionWithBuild.second.category
                                ),
                                isLoading = false
                            )
                        }
                    }
            }
        } else {
            val newTransaction = Transaction(Clock.System.now())
            state.update { TransactionUiState(newTransaction, isModified = true) }
        }
    }

    fun updateTransactionDateTime(dateTime: Instant) {
        state.update {
            it.copy(
                entity = it.entity?.copy(dataTime = dateTime).also { self -> self!!.id = it.entity!!.id },
                isModified = true
            )
        }
    }

    fun updateTransactionExpense(accountCurrency: AccountCurrency?, amount: Double?) {
        state.update {
            it.copy(
                entity = it.entity?.copy(
                    expenseAccountCurrency = accountCurrency,
                    expenseAmount = amount
                ).also { self -> self!!.id = it.entity!!.id },
                isModified = true
            )
        }
    }

    fun updateTransactionIncome(accountCurrency: AccountCurrency?, amount: Double?) {
        state.update {
            it.copy(
                entity = it.entity?.copy(
                    incomeAccountCurrency = accountCurrency,
                    incomeAmount = amount
                ).also { self -> self!!.id = it.entity!!.id },
                isModified = true
            )
        }
    }

    fun updateTransactionCategory(category: Category?) {
        state.update {
            it.copy(
                entity = it.entity?.copy(category = category).also { self -> self!!.id = it.entity!!.id },
                isModified = true
            )
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.insert(transaction.mapToTransaction())
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.update(transaction.mapToTransaction())
        }
    }
}

data class TransactionUiState(
    override val entity: Transaction? = null,
    override val isModified: Boolean = false,
    override val isLoading: Boolean = false,
    override val errors: Map<String, String> = emptyMap()

) : DetailsState<Transaction>

