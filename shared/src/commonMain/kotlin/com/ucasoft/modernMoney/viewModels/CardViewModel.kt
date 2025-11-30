package com.ucasoft.modernMoney.viewModels

import androidx.lifecycle.ViewModel
import com.ucasoft.modernMoney.model.AccountCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CardViewModel : ViewModel() {

    private val _state = MutableStateFlow(CardUiState())
    val state = _state.asStateFlow()

    init {
        val newCard = AccountCard("", "")
        _state.update { CardUiState(newCard, validate(newCard)) }
    }

    fun updateCardType(type: String) {
        _state.update {
            val firstCopy = it.copy(
                card = it.card!!.copy(type = type)
            )
            firstCopy.copy(
                errors = validate(firstCopy.card!!)
            )
        }
    }

    fun updateCardNumber(number: String) {
        _state.update {
            val firstCopy = it.copy(
                card = it.card!!.copy(number = number)
            )
            firstCopy.copy(
                errors = validate(firstCopy.card!!)
            )
        }
    }

    private fun validate(card: AccountCard): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        if (card.type.isBlank()) {
            errors["type"] = "Choose type of card!"
        }

        if (card.number.length != 4 || card.number.any { !it.isDigit() }) {
            errors["number"] = "Card number must contain exact 4 digits!"
        }

        return errors
    }
}

data class CardUiState(
    val card: AccountCard? = null,
    override val errors: Map<String, String> = emptyMap()
): StateWithErrors