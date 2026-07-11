package com.ucasoft.modernMoney.db.repositories

import com.ucasoft.modernMoney.db.dto.PayeeDao
import com.ucasoft.modernMoney.model.toPayee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PayeeRepository(payeeDao: PayeeDao, scope: CoroutineScope) {
    val payees = payeeDao.allPayees()
        .map { it.associate { it.id to it.toPayee() } }
        .stateIn(
            scope,
            SharingStarted.Eagerly,
            emptyMap()
        )
}