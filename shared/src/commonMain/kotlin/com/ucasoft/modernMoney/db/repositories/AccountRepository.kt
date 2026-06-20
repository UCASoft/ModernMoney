package com.ucasoft.modernMoney.db.repositories

import com.ucasoft.modernMoney.db.dto.AccountDao
import com.ucasoft.modernMoney.model.AccountMapContext
import com.ucasoft.modernMoney.model.toAccount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class AccountRepository(accountDao: AccountDao, bankRepository: BankRepository, scope: CoroutineScope) {

    val accounts = accountDao.allAccounts()
        .combine(bankRepository.banks) { accounts, banks ->
            accounts.associate { it.account.id to it.toAccount(AccountMapContext(banks)) }
        }.stateIn(
            scope,
            started = SharingStarted.Eagerly,
            initialValue = emptyMap()
        )
}