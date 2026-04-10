package com.ucasoft.modernMoney.viewModels.transaction

import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.TransactionDao
import com.ucasoft.modernMoney.model.Transaction
import com.ucasoft.modernMoney.viewModels.ListState
import com.ucasoft.modernMoney.viewModels.ListViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Clock

class TransactionsViewModel(private val transactionDao: TransactionDao): ListViewModel<Transaction, TransactionsUiState>() {

    override val listState = transactionDao.allTransaction().map {
        /*TransactionsUiState(it.map {
            it.transaction.mapToTransaction(it.category?.mapToCategory())
        })*/
        TransactionsUiState(
            listOf(
                Transaction(
                    Clock.System.now(),
                    2,
                    250.0,
                    comment = "Initial transaction"
                ),
                Transaction(
                    Clock.System.now(),
                    incomeCurrencyId = 2,
                    incomeAmount = 250.0
                ).also { it.id = 1 },
                Transaction(
                    Clock.System.now(),
                    2,
                    250.0,
                    5,
                    250.0
                ).also { it.id = 2 }
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TransactionsUiState(isLoading = true)
    )

}

data class TransactionsUiState(
    override val items: List<Transaction> = emptyList(),
    override val isLoading: Boolean = false
) : ListState<Transaction>