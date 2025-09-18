package com.ucasoft.modernMoney.db.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.AccountDao
import com.ucasoft.modernMoney.db.model.Account
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountViewModel(private val accountDao: AccountDao) : ViewModel() {

    val uiState = accountDao.allAccounts().map {
        AccountUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = AccountUiState(isLoading = true)
    )

    fun addAccount(account: Account) {
        viewModelScope.launch {
            accountDao.insert(account)
        }
    }
}

data class AccountUiState(
    val accounts: List<Account> = emptyList(),
    val isLoading: Boolean = false
)