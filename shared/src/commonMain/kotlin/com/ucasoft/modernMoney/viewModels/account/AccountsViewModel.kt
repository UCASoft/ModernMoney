package com.ucasoft.modernMoney.viewModels.account

import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.AccountCurrencyDao
import com.ucasoft.modernMoney.db.dto.AccountDao
import com.ucasoft.modernMoney.db.dto.TransactionDao
import com.ucasoft.modernMoney.db.repositories.BankRepository
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountMapContext
import com.ucasoft.modernMoney.model.toAccount
import com.ucasoft.modernMoney.viewModels.ListState
import com.ucasoft.modernMoney.viewModels.ReorderingViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountsViewModel(
    private val accountDao: AccountDao,
    bankRepository: BankRepository,
    private val transactionDao: TransactionDao,
    accountCurrencyDao: AccountCurrencyDao
) : ReorderingViewModel<Account, AccountsUiState>() {

    override val listState = accountDao.allAccounts()
        .combine(bankRepository.banks) { accounts, banks -> accounts to banks }
        .combine(accountCurrencyDao.currencyBalances()) { (accounts, banks), balances ->
            val balanceByCurrencyId = balances.associate { it.currencyId to it.balance }
            AccountsUiState(accounts.map {
                it.toAccount(AccountMapContext(banks)).also { account ->
                    account.currencies.forEach {
                        it.balance = balanceByCurrencyId[it.id] ?: 0.0
                    }
                }
            })
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AccountsUiState(isLoading = true)
        )

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            accountDao.delete(account.toAccount())
            transactionDao.clearTransaction()
        }
    }

    override fun reorderItems(from: Int, to: Int) {
        viewModelScope.launch {
            accountDao.reorderItems(from, to)
        }
    }
}

data class AccountsUiState(
    override val items: List<Account> = emptyList(),
    override val isLoading: Boolean = false
) : ListState<Account>