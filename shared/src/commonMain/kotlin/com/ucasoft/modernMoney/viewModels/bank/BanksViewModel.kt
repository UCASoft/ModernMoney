import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.BankDao
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.model.mapToBank
import com.ucasoft.modernMoney.viewModels.ListState
import com.ucasoft.modernMoney.viewModels.ListViewModel
import com.ucasoft.modernMoney.viewModels.bank.BankUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BanksViewModel(private val bankDao: BankDao): ListViewModel<Bank, BanksUiState>() {

    override val listState = bankDao.allBanks().map {
        BanksUiState(it.map { it.mapToBank() })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BanksUiState(isLoading = true)
    )

    fun deleteBank(bank: Bank) {
        viewModelScope.launch {
            bankDao.delete(bank.mapToBank())
        }
    }
}

data class BanksUiState(
    override val items: List<Bank> = emptyList(),
    override val isLoading: Boolean = false
) : ListState<Bank>