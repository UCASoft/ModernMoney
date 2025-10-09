import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.BankDao
import com.ucasoft.modernMoney.model.Bank
import com.ucasoft.modernMoney.model.mapToBank
import com.ucasoft.modernMoney.viewModels.bank.BankUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BanksViewModel(private val bankDao: BankDao): ViewModel() {
    val state = bankDao.allBanks().map {
        BanksUiState(it.map { it.mapToBank() })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BanksUiState(isLoading = true)
    )
}

data class BanksUiState(
    val banks: List<Bank> = emptyList(),
    val isLoading: Boolean = false
)