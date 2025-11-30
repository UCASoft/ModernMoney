package com.ucasoft.modernMoney.viewModels.account

import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.AccountCardDao
import com.ucasoft.modernMoney.db.dto.AccountCurrencyDao
import com.ucasoft.modernMoney.db.dto.AccountDao
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountCard
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.model.mapToAccount
import com.ucasoft.modernMoney.viewModels.DetailViewModel
import com.ucasoft.modernMoney.viewModels.DetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(private val accountDao: AccountDao, private val accountCurrencyDao: AccountCurrencyDao, private val accountCardDao: AccountCardDao, id: Long?) : DetailViewModel<Account, AccountUiState>() {

    private val _state = MutableStateFlow(AccountUiState(isLoading = true))
    override val state = _state.asStateFlow()

    private val allAccounts = mutableListOf<Account>()

    init {
        viewModelScope.launch {
            accountDao.allAccounts().collect { allAccounts.addAll(it.map { it.account.mapToAccount(it.currencies, it.bank, it.cards) }) }
        }
        if (id != null) {
            viewModelScope.launch {
                accountDao.accountById(id).collect { account ->
                    _state.update { it.copy(entity = account.account.mapToAccount(account.currencies, account.bank, account.cards), isLoading = false) }
                }
            }
        } else {
            val newAccount = Account()
            _state.update { AccountUiState(newAccount, isModified = true, errors = validate(newAccount)) }
        }
    }

    fun addAccount(account: Account) {
        viewModelScope.launch {
            val accountId = accountDao.insert(account.mapToDbAccount())
            account.currencies.forEach {
                accountCurrencyDao.insert(it.mapToDbAccountCurrency(accountId))
            }
            account.cards.forEach {
                accountCardDao.insert(it.mapToDbAccountCard(accountId))
            }
        }
    }

    fun updateAccount(account: Account) {
        viewModelScope.launch {
            accountDao.update(account.mapToDbAccount())
            account.currencies.filter { it.id == 0L }.forEach {
                accountCurrencyDao.insert(it.mapToDbAccountCurrency(account.id))
            }
            accountCardDao.refreshCards(account.id, account.cards.map { it.mapToDbAccountCard(account.id) })
        }
    }

    fun updateAccountName(name: String) {
        _state.update {
            val copy = it.copy(
                entity = it.entity?.copy(name = name).also { self -> self!!.id = it.entity!!.id },
                isModified = true
            )
            copy.copy(
                errors = validate(copy.entity!!)
            )
        }
    }

    fun updateAccountBank(bank: Bank?) {
        _state.update { it.copy(
            entity = it.entity?.copy(bank = bank).also { self -> self!!.id = it.entity!!.id },
            isModified = true
        ) }
    }

    fun addAccountCurrency(currency: AccountCurrency) {
        _state.update {
            val copy = it.copy(
                entity = it.entity?.copy(currencies = it.entity.currencies + currency).also { self -> self!!.id = it.entity!!.id },
                isModified = true
            )
            copy.copy(
                errors = validate(copy.entity!!)
            )
        }
    }

    fun deleteAccountCurrency(currency: AccountCurrency) {
        _state.update {
            val copy = it.copy(
                entity = it.entity?.copy(currencies = it.entity.currencies.filter { it != currency }).also { self -> self!!.id = it.entity!!.id },
                isModified = true
            )
            copy.copy(
                errors = validate(copy.entity!!)
            )
        }
    }

    fun addCard(card: AccountCard) {
        _state.update {
            it.copy(
                entity = it.entity!!.copy(cards = it.entity.cards + card).also { self -> self.id = it.entity.id }
            )
        }
    }

    fun deleteCard(card: AccountCard) {
        _state.update {
            it.copy(
                entity = it.entity!!.copy(cards = it.entity.cards.filter { it != card }).also { self -> self.id = it.entity.id }
            )
        }
    }

    private fun validate(account: Account): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        when {
            account.name.isBlank() -> errors["name"] = "Name cannot be empty or blank!"
            allAccounts.any { it.name == account.name } -> errors["name"] = "Account with name ${account.name} already exists!"
        }

        when {
            account.currencies.isEmpty() -> errors["currencies"] = "Account must contain at least one currency!"
        }

        return errors
    }
}

data class AccountUiState(
    override val entity: Account? = null,
    override val isModified: Boolean = false,
    override val isLoading: Boolean = false,
    override val errors: Map<String, String> = emptyMap()
) : DetailsState<Account>