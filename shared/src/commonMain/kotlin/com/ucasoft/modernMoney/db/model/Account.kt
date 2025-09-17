package com.ucasoft.modernMoney.db.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Entity
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val currency: String
)

@Dao
interface AccountDao {

    @Query("SELECT * FROM account")
    fun allAccounts() : Flow<List<Account>>

    @Insert
    suspend fun insert(account: Account)
}

data class AccountUiState(
    val accounts: List<Account> = emptyList(),
    val isLoading: Boolean = false
)

class AccountViewModel(private val accountDao: AccountDao) : ViewModel() {

    val uiState = accountDao.allAccounts().map {
        AccountUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AccountUiState(isLoading = true)
    )

    fun addAccount(account: Account) {
        viewModelScope.launch {
            accountDao.insert(account)
        }
    }
}