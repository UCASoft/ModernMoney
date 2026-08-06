package com.ucasoft.modernMoney.viewModels.transaction

import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.TransactionDao
import com.ucasoft.modernMoney.db.filters.TransactionFilter
import com.ucasoft.modernMoney.db.repositories.AccountCurrencyRepository
import com.ucasoft.modernMoney.db.repositories.AccountRepository
import com.ucasoft.modernMoney.db.repositories.CategoryRepository
import com.ucasoft.modernMoney.db.repositories.PayeeRepository
import com.ucasoft.modernMoney.model.Transaction
import com.ucasoft.modernMoney.model.TransactionMapContext
import com.ucasoft.modernMoney.model.toTransaction
import com.ucasoft.modernMoney.viewModels.FilteredListViewModel
import com.ucasoft.modernMoney.viewModels.ListState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.ucasoft.modernMoney.db.model.Transaction as DbTransaction

class TransactionsViewModel(
    private val transactionDao: TransactionDao,
    private val accountCurrencyRepository: AccountCurrencyRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val payeeRepository: PayeeRepository
): FilteredListViewModel<Transaction, TransactionsUiState, TransactionFilter>(
    ::TransactionFilter
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val listState =
        filter
            .flatMapLatest {
                combineTransactions(transactionDao.transactionsByFilter(it))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = TransactionsUiState(isLoading = true)
            )

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.delete(transaction.mapToTransaction())
        }
    }

    private fun combineTransactions(
        transactionFlow: Flow<List<DbTransaction>>
    ): Flow<TransactionsUiState> =
        combine(transactionFlow, accountRepository.accounts, accountCurrencyRepository.accountCurrencies, categoryRepository.categories, payeeRepository.payees) {
            transactions, accounts, accountCurrencies, categories, payees ->
            TransactionsUiState(
                transactions.map {
                    it.toTransaction(TransactionMapContext(accounts, accountCurrencies, categories, payees))
                }
            )
        }
}

data class TransactionsUiState(
    override val items: List<Transaction> = emptyList(),
    override val isLoading: Boolean = false
) : ListState<Transaction>