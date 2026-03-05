package com.ucasoft.modernMoney.viewModels.payee

import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.PayeeDao
import com.ucasoft.modernMoney.model.Payee
import com.ucasoft.modernMoney.model.mapToPayee
import com.ucasoft.modernMoney.viewModels.ListState
import com.ucasoft.modernMoney.viewModels.ListViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PayeesViewModel(private val payeeDao: PayeeDao) : ListViewModel<Payee, PayeesUiState>() {

    override val listState = payeeDao.allPayees().map {
        PayeesUiState(it.map { it.mapToPayee() })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PayeesUiState(isLoading = true)
    )

    fun deletePayee(payee: Payee) {
        viewModelScope.launch {
            payeeDao.delete(payee.mapToPayee())
        }
    }
}

data class PayeesUiState(
    override val items: List<Payee> = emptyList(),
    override val isLoading: Boolean = false
) : ListState<Payee>