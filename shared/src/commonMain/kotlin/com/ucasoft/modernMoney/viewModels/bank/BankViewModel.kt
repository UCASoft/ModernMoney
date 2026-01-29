package com.ucasoft.modernMoney.viewModels.bank

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.BankDao
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.model.mapToBank
import com.ucasoft.modernMoney.viewModels.LogoDetailsState
import com.ucasoft.modernMoney.viewModels.LogoEntityViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BankViewModel(private val bankDao: BankDao, id: Long?): LogoEntityViewModel<Bank, BankUiState>() {

    override val stateFlow = MutableStateFlow(BankUiState(isLoading = true))
    override val state = stateFlow.asStateFlow()

    private val allBanks = mutableListOf<Bank>()

    init {
        viewModelScope.launch {
            bankDao.allBanks().collect { allBanks.addAll(it.map { it.mapToBank() }) }
        }
        if (id != null) {
            viewModelScope.launch {
                bankDao.bankById(id).collect { bank ->
                    stateFlow.update { it.copy(entity = bank.mapToBank(), isLoading = false) }
                }
            }
        } else {
            val newBank = Bank("")
            stateFlow.update { BankUiState(newBank, isModified = true, errors = validate(newBank, allBanks)) }
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
        stateFlow.update { it.copy(
            isModified = false
        ) }
    }

    fun updateBankName(name: String) {
        stateFlow.update {
            val firstCopy = it.copy(
                entity = it.entity?.copy(name = name).also { self -> self!!.id = it.entity!!.id },
                isModified = true
            )
            firstCopy.copy(
                errors = validate(firstCopy.entity!!, allBanks)
            )
        }
    }

    private fun validate(bank: Bank, others: List<Bank>) = when {
            bank.name.isBlank() -> mapOf("name" to "Name cannot be empty or blank!")
            others.any { it.name == bank.name && it.id != bank.id } -> mapOf("name" to "Bank with name ${bank.name} already exists!")
            else -> emptyMap()
        }
}

data class BankUiState(
    override val entity: Bank? = null,
    override val isModified: Boolean = false,
    override val isLoading: Boolean = false,
    override val errors: Map<String, String> = emptyMap()
) : LogoDetailsState<Bank> {

    override fun updateLogo(logo: ImageBitmap?): BankUiState {
        return copy(
            entity = entity?.copy(logo = logo).also { self -> self!!.id = entity!!.id  },
            isModified = true
        )
    }
}