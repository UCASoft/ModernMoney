package com.ucasoft.modernMoney.db.repositories

import com.ucasoft.modernMoney.db.dto.BankDao
import com.ucasoft.modernMoney.model.mapToBank
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BankRepository(bankDao: BankDao, scope: CoroutineScope) {

    val banks = bankDao.allBanks()
        .map { it.associate { it.id to it.mapToBank() } }
        .stateIn(
            scope,
            started = SharingStarted.Eagerly,
            initialValue = emptyMap()
        )
}