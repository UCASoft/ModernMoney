package com.ucasoft.modernMoney.viewModels.bank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.BankDao
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.model.mapToBank
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BankViewModel(private val bankDao: BankDao, id: Long): ViewModel() {

    private val _state = MutableStateFlow(BankUiState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            bankDao.bankById(id).collect { bank ->
                _state.update { it.copy(bank = bank.mapToBank(), isLoading = false) }
            }
        }
    }

    fun addBank(bank: Bank) {
        viewModelScope.launch {
            bankDao.insert(bank.mapToBank())
        }
    }

    fun updateBank(bank: Bank) {
        viewModelScope.launch {
            bankDao.update(bank.mapToBank())
        }
        _state.update { it.copy(
            isModified = false
        ) }
    }

    fun deleteBank(bank: Bank) {
        viewModelScope.launch {
            bankDao.delete(bank.mapToBank())
        }
    }

    fun updateBankName(name: String) {
        _state.update { it.copy(
            bank = it.bank?.copy(name = name).also { self -> self!!.id = it.bank!!.id },
            isModified = true
        ) }
    }
}

data class BankUiState(
    val bank: Bank? = null,
    val isModified: Boolean = false,
    val isLoading: Boolean = false
)