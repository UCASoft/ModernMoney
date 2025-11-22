package com.ucasoft.modernMoney.viewModels.account

import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.AccountCurrencyDao
import com.ucasoft.modernMoney.db.dto.AccountDao
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.model.mapToAccount
import com.ucasoft.modernMoney.model.mapToBank
import com.ucasoft.modernMoney.viewModels.DetailViewModel
import com.ucasoft.modernMoney.viewModels.DetailsState
import com.ucasoft.modernMoney.viewModels.ListState
import com.ucasoft.modernMoney.viewModels.ListViewModel
import com.ucasoft.modernMoney.viewModels.bank.BankUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(private val accountDao: AccountDao, private val accountCurrencyDao: AccountCurrencyDao, id: Long?) : DetailViewModel<Account, AccountUiState>() {

    private val _state = MutableStateFlow(AccountUiState(isLoading = true))
    override val state = _state.asStateFlow()

    init {
        if (id != null) {
            viewModelScope.launch {
                accountDao.accountById(id).collect { account ->
                    _state.update { it.copy(entity = account.account.mapToAccount(account.currencies, account.bank), isLoading = false) }
                }
            }
        } else {
            _state.update { AccountUiState(Account(), isModified = true) }
        }
    }

    fun addAccount(account: Account) {
        viewModelScope.launch {
            val accountId = accountDao.insert(account.mapToDbAccount())
            account.currencies.forEach {
                accountCurrencyDao.insert(it.mapToDbAccountCurrency(accountId))
            }
        }
    }

    fun updateAccount(account: Account) {
        viewModelScope.launch {
            accountDao.update(account.mapToDbAccount())
            account.currencies.filter { it.id == 0L }.forEach {
                accountCurrencyDao.insert(it.mapToDbAccountCurrency(account.id))
            }
        }
    }

    fun updateAccountName(name: String) {
        _state.update { it.copy(
            entity = it.entity?.copy(name = name).also { self -> self!!.id = it.entity!!.id },
            isModified = true
        ) }
    }

    fun updateAccountBank(bank: Bank?) {
        _state.update { it.copy(
            entity = it.entity?.copy(bank = bank).also { self -> self!!.id = it.entity!!.id },
            isModified = true
        ) }
    }

    fun addAccountCurrency(currency: AccountCurrency) {
        _state.update { it.copy(
            entity = it.entity?.copy(currencies = it.entity.currencies + currency).also { self -> self!!.id = it.entity!!.id },
            isModified = true
        ) }
    }

    fun deleteAccountCurrency(currency: AccountCurrency) {
        _state.update { it.copy(
            entity = it.entity?.copy(currencies = it.entity.currencies.filter { it != currency }).also { self -> self!!.id = it.entity!!.id },
            isModified = true
        ) }
    }
}

data class AccountUiState(
    override val entity: Account? = null,
    override val isModified: Boolean = false,
    override val isLoading: Boolean = false,
    override val errors: Map<String, String> = emptyMap()
) : DetailsState<Account>