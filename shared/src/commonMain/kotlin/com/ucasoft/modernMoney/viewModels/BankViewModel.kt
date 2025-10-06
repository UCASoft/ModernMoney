package com.ucasoft.modernMoney.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.BankDao
import com.ucasoft.modernMoney.model.Bank
import kotlinx.coroutines.launch

class BankViewModel(private val bankDao: BankDao): ViewModel() {

    fun addBank(bank: Bank) {
        viewModelScope.launch {
            bankDao.insert(bank.mapToBank())
        }
    }
}