package com.ucasoft.modernMoney.viewModels.transaction

import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.TransactionDao
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.model.Transaction
import com.ucasoft.modernMoney.viewModels.DetailViewModel
import com.ucasoft.modernMoney.viewModels.DetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Instant

class TransactionViewModel(private val transactionDao: TransactionDao, id: Long?) : DetailViewModel<Transaction, TransactionUiState>() {
    override val state: StateFlow<TransactionUiState>
        field = MutableStateFlow(TransactionUiState(isLoading = true))

    init {
        if (id != null) {
            viewModelScope.launch {

            }
        } else {
            val newTransaction = Transaction(Clock.System.now())
            state.update { TransactionUiState(newTransaction, isModified = true) }
        }
    }

    fun updateTransactionDateTime(dateTime: Instant) {
        state.update {
            it.copy(
                entity = it.entity?.copy(dataTime = dateTime),
                isModified = true
            )
        }
    }

    fun updateTransactionExpense(accountCurrency: AccountCurrency, amount: Double?) {
        state.update {
            it.copy(
                entity = it.entity?.copy(
                    expenseAccountCurrency = accountCurrency,
                    expenseAmount = amount
                ),
                isModified = true
            )
        }
    }

    fun updateTransactionIncome(accountCurrency: AccountCurrency, amount: Double?) {
        state.update {
            it.copy(
                entity = it.entity?.copy(
                    incomeAccountCurrency = accountCurrency,
                    incomeAmount = amount
                ),
                isModified = true
            )
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.insert(transaction.mapToTransaction())
        }
    }
}

data class TransactionUiState(
    override val entity: Transaction? = null,
    override val isModified: Boolean = false,
    override val isLoading: Boolean = false,
    override val errors: Map<String, String> = emptyMap()

) : DetailsState<Transaction>
