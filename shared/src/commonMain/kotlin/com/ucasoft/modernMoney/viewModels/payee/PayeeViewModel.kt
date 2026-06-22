package com.ucasoft.modernMoney.viewModels.payee

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import com.ucasoft.modernMoney.db.dto.PayeeDao
import com.ucasoft.modernMoney.model.Payee
import com.ucasoft.modernMoney.model.toPayee
import com.ucasoft.modernMoney.viewModels.LogoDetailsState
import com.ucasoft.modernMoney.viewModels.LogoEntityViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PayeeViewModel(private val payeeDao: PayeeDao, id: Long?) : LogoEntityViewModel<Payee, PayeeUiState>() {
    override val stateFlow = MutableStateFlow(PayeeUiState(isLoading = true))
    override val state = stateFlow.asStateFlow()

    private val allPayeesAliases = mutableMapOf<String, String>()

    init {
        viewModelScope.launch {
            payeeDao.allPayees().collect {
                it.filterNot { it.id == id }.forEach { payee ->
                    payee.aliases.forEach {
                        allPayeesAliases[it] = payee.name
                    }
                }
            }
        }
        if (id != null) {
            viewModelScope.launch {
                payeeDao.payeeById(id).collect { payee ->
                    stateFlow.update { it.copy(entity = payee.toPayee(), isLoading = false) }
                }
            }
        } else {
            val newPayee = Payee("")
            stateFlow.update { PayeeUiState(newPayee, isModified = true, errors = validate(newPayee)) }
        }
    }

    fun addPayee(payee: Payee) {
        viewModelScope.launch {
            payeeDao.insert(payee.toPayee())
        }
    }

    fun updatePayee(payee: Payee) {
        viewModelScope.launch {
            payeeDao.update(payee.toPayee())
        }
        stateFlow.update {
            it.copy(
                isModified = false
            )
        }
    }

    fun updatePayeeName(name: String) {
        stateFlow.update {
            val firstCopy = it.copy(
                entity = it.entity?.copy(name = name).also { self -> self!!.id = it.entity!!.id },
                isModified = true
            )
            firstCopy.copy(
                errors = validate(firstCopy.entity!!)
            )
        }
    }

    fun addPayeeAlias(alias: String) {
        if (stateFlow.value.entity?.aliases?.contains(alias) == true) {
            return
        }
        stateFlow.update {
            val firstCopy = it.copy(
                entity = it.entity?.copy(aliases = it.entity.aliases + alias).also { self -> self!!.id = it.entity!!.id },
                isModified = true
            )
            firstCopy.copy(
                errors = validate(firstCopy.entity!!)
            )
        }
    }

    fun deletePayeeAlias(alias: String) {
        stateFlow.update {
            val firstCopy = it.copy(
                entity = it.entity?.copy(aliases = it.entity.aliases - alias).also { self -> self!!.id = it.entity!!.id },
                isModified = true
            )
            firstCopy.copy(
                errors = validate(firstCopy.entity!!)
            )
        }
    }

    private fun validate(payee: Payee) = when {
        payee.name.isBlank() -> mapOf("name" to "Name cannot be empty or blank!")
        allPayeesAliases.values.contains(payee.name) -> mapOf("name" to "Payee with name ${payee.name} already exists!")
        payee.aliases.groupBy { it }.any { it.value.size > 1 } -> mapOf("aliases" to "Aliases must be unique!")
        payee.aliases.any { allPayeesAliases.containsKey(it) } -> {
            val alias = allPayeesAliases.filter { payee.aliases.contains(it.key) }.toList().first()
            mapOf("aliases" to "Alias ${alias.first} is already exists in ${alias.second}")
            
        }
        else -> emptyMap()
    }
}

data class PayeeUiState(
    override val entity: Payee? = null,
    override val isModified: Boolean = false,
    override val isLoading: Boolean = false,
    override val errors: Map<String, String> = emptyMap()
) : LogoDetailsState<Payee> {

    override fun updateLogo(logo: ImageBitmap?): PayeeUiState {
        return copy(
            entity = entity?.copy(logo = logo).also { self -> self!!.id = entity!!.id  },
            isModified = true
        )
    }
}