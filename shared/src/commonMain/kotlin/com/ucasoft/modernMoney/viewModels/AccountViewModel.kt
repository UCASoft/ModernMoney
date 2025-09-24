package com.ucasoft.modernMoney.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.AccountCurrencyDao
import com.ucasoft.modernMoney.db.dto.AccountDao
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.mapToAccount
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountViewModel(private val accountDao: AccountDao, private val accountCurrencyDao: AccountCurrencyDao) : ViewModel() {

    val uiState = accountDao.allAccounts().map {
        AccountUiState(it.map {
            it.mapToAccount(accountCurrencyDao.accountCurrency(it.id))
        })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = AccountUiState(isLoading = true)
    )

    fun addAccount(account: Account) {
        viewModelScope.launch {
            val accountId = accountDao.insert(account.mapToDbAccount())
            account.currencies.forEach {
                accountCurrencyDao.insert(it.mapToDbAccountCurrency(accountId))
            }
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            accountDao.delete(account.mapToDbAccount())
        }
    }
}

data class AccountUiState(
    val accounts: List<Account> = emptyList(),
    val isLoading: Boolean = false
)