package com.ucasoft.modernMoney.viewModels.account

import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.AccountCardDao
import com.ucasoft.modernMoney.db.dto.AccountCurrencyDao
import com.ucasoft.modernMoney.db.dto.AccountDao
import com.ucasoft.modernMoney.db.repositories.BankRepository
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.model.AccountCard
import com.ucasoft.modernMoney.model.AccountIdContext
import com.ucasoft.modernMoney.model.AccountCurrency
import com.ucasoft.modernMoney.model.AccountMapContext
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.model.toAccount
import com.ucasoft.modernMoney.model.toAccountCard
import com.ucasoft.modernMoney.model.toAccountCurrency
import com.ucasoft.modernMoney.viewModels.DetailViewModel
import com.ucasoft.modernMoney.viewModels.DetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val accountDao: AccountDao,
    private val bankRepository: BankRepository,
    private val accountCurrencyDao: AccountCurrencyDao,
    private val accountCardDao: AccountCardDao, id: Long?
) : DetailViewModel<Account, AccountUiState>() {

    override val state: StateFlow<AccountUiState>
        field = MutableStateFlow(AccountUiState(isLoading = true))

    private val allAccounts = mutableListOf<Account>()

    init {
        viewModelScope.launch {
            accountDao.allAccounts()
                .combine(bankRepository.banks) { accounts, banks ->
                    accounts to banks
                }
                .collect { (accounts, banks) ->
                    allAccounts.clear()
                    allAccounts.addAll(accounts.filterNot { it.account.id == id }
                        .map { it.toAccount(AccountMapContext(banks)) })
                    if (id == null) {
                        val newAccount = Account(order = allAccounts.size)
                        state.update { AccountUiState(newAccount, isModified = true, errors = validate(newAccount)) }
                    }
                }
        }
        if (id != null) {
            viewModelScope.launch {
                accountDao.accountById(id)
                    .combine(bankRepository.banks) { account, banks ->
                        account to banks
                    }
                    .collect { (account, banks) ->
                        state.update {
                            it.copy(
                                entity = account.toAccount(AccountMapContext(banks)), isLoading = false
                            )
                        }
                    }
            }
        }
    }


    fun addAccount(account: Account) {
        viewModelScope.launch {
            val accountId = accountDao.insert(account.toAccount())
            account.currencies.forEach {
                accountCurrencyDao.insert(it.toAccountCurrency(AccountIdContext(accountId)))
            }
            account.cards.forEach {
                accountCardDao.insert(it.toAccountCard(AccountIdContext(accountId)))
            }
        }
    }

    fun updateAccount(account: Account) {
        viewModelScope.launch {
            accountDao.update(account.toAccount())
            account.currencies.filter { it.id == 0L }.forEach {
                accountCurrencyDao.insert(it.toAccountCurrency(AccountIdContext(account.id)))
            }
            accountCardDao.refreshCards(account.id, account.cards.map { it.toAccountCard(AccountIdContext(account.id)) })
        }
    }

    fun updateAccountName(name: String) {
        state.update {
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
        state.update {
            it.copy(
                entity = it.entity?.copy(bank = bank).also { self -> self!!.id = it.entity!!.id },
                isModified = true
            )
        }
    }

    fun addAccountCurrency(currency: AccountCurrency) {
        state.update {
            val copy = it.copy(
                entity = it.entity?.copy(currencies = it.entity.currencies + currency)
                    .also { self -> self!!.id = it.entity!!.id },
                isModified = true
            )
            copy.copy(
                errors = validate(copy.entity!!)
            )
        }
    }

    fun deleteAccountCurrency(currency: AccountCurrency) {
        state.update {
            val copy = it.copy(
                entity = it.entity?.copy(currencies = it.entity.currencies.filter { it != currency })
                    .also { self -> self!!.id = it.entity!!.id },
                isModified = true
            )
            copy.copy(
                errors = validate(copy.entity!!)
            )
        }
    }

    fun addCard(card: AccountCard) {
        state.update {
            it.copy(
                entity = it.entity!!.copy(cards = it.entity.cards + card).also { self -> self.id = it.entity.id }
            )
        }
    }

    fun deleteCard(card: AccountCard) {
        state.update {
            it.copy(
                entity = it.entity!!.copy(cards = it.entity.cards.filter { it != card })
                    .also { self -> self.id = it.entity.id }
            )
        }
    }

    private fun validate(account: Account): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        when {
            account.name.isBlank() -> errors["name"] = "Name cannot be empty or blank!"
            allAccounts.any { it.name == account.name } -> errors["name"] =
                "Account with name ${account.name} already exists!"
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