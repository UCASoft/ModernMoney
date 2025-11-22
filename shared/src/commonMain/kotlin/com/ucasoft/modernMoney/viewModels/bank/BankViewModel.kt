package com.ucasoft.modernMoney.viewModels.bank

import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.BankDao
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.model.mapToBank
import com.ucasoft.modernMoney.viewModels.DetailViewModel
import com.ucasoft.modernMoney.viewModels.DetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BankViewModel(private val bankDao: BankDao, id: Long?): DetailViewModel<Bank, BankUiState>() {

    private val _state = MutableStateFlow(BankUiState(isLoading = true))
    override val state = _state.asStateFlow()

    private val allBanks = mutableListOf<Bank>()

    init {
        viewModelScope.launch {
            bankDao.allBanks().collect { allBanks.addAll(it.map { it.mapToBank() }) }
        }
        if (id != null) {
            viewModelScope.launch {
                bankDao.bankById(id).collect { bank ->
                    _state.update { it.copy(entity = bank.mapToBank(), isLoading = false) }
                }
            }
        } else {
            val newBank = Bank("")
            _state.update { BankUiState(newBank, isModified = true, errors = validate(newBank, emptyList())) }
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

    fun updateBankName(name: String) {
        _state.update {
            val firstCopy = it.copy(
                entity = it.entity?.copy(name = name).also { self -> self!!.id = it.entity!!.id },
                isModified = true
            )
            firstCopy.copy(
                errors = validate(firstCopy.entity!!, allBanks)
            )
        }
    }

    fun validate(bank: Bank, others: List<Bank>) = when {
            bank.name.isBlank() -> mapOf("name" to "Name cannot be empty or blank!")
            others.any { it.name == bank.name } -> mapOf("name" to "Bank with name ${bank.name} already exists!")
            else -> emptyMap()
        }
}

data class BankUiState(
    override val entity: Bank? = null,
    override val isModified: Boolean = false,
    override val isLoading: Boolean = false,
    override val errors: Map<String, String> = emptyMap()
) : DetailsState<Bank>