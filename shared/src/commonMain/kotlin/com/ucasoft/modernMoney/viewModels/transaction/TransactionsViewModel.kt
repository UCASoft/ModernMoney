package com.ucasoft.modernMoney.viewModels.transaction

import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.TransactionDao
import com.ucasoft.modernMoney.db.model.Transaction as DbTransaction
import com.ucasoft.modernMoney.db.repositories.AccountCurrencyRepository
import com.ucasoft.modernMoney.db.repositories.AccountRepository
import com.ucasoft.modernMoney.db.repositories.CategoryRepository
import com.ucasoft.modernMoney.model.Transaction
import com.ucasoft.modernMoney.model.mapToTransaction
import com.ucasoft.modernMoney.viewModels.ListState
import com.ucasoft.modernMoney.viewModels.ListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val transactionDao: TransactionDao,
    private val accountCurrencyRepository: AccountCurrencyRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository
): ListViewModel<Transaction, TransactionsUiState>() {

    private val transactionFilter = MutableStateFlow<Long?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val listState =
        transactionFilter
            .flatMapLatest { accountId ->
                val flow = if (accountId == null) {
                    transactionDao.allTransaction()
                } else {
                    transactionDao.transactionsByAccountId(accountId)
                }
                combineTransactions(flow)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = TransactionsUiState(isLoading = true)
            )


    fun setFilter(accountId: Long?) {
        transactionFilter.value = accountId
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.delete(transaction.mapToTransaction())
        }
    }

    private fun combineTransactions(
        transactionFlow: Flow<List<DbTransaction>>
    ): Flow<TransactionsUiState> = transactionFlow
        .combine(accountCurrencyRepository.accountCurrencies) { transactions, accountCurrencies ->
            transactions.map {
                it to BuildTransaction(
                    it.expenseCurrencyId?.let { accountCurrencies[it] },
                    it.incomeCurrencyId?.let { accountCurrencies[it] }
                )
            }
        }
        .combine(categoryRepository.categories) { transactionsWithBuild, categories ->
            transactionsWithBuild.map { (transaction, build) ->
                transaction to build.copy(
                    category = transaction.categoryId?.let { categories[it] }
                )
            }
        }
        .combine(accountRepository.accounts) { transactionsWithBuild, accounts ->
            TransactionsUiState(
                transactionsWithBuild.map { (transaction, build) ->
                    transaction.mapToTransaction(
                        expenseAccount = build.expenseAccountCurrency?.let { accounts[it.accountId] },
                        expenseAccountCurrency = build.expenseAccountCurrency,
                        incomeAccount = build.incomeAccountCurrency?.let { accounts[it.accountId] },
                        incomeAccountCurrency = build.incomeAccountCurrency,
                        build.category
                    )
                }
            )
        }
}

data class TransactionsUiState(
    override val items: List<Transaction> = emptyList(),
    override val isLoading: Boolean = false
) : ListState<Transaction>