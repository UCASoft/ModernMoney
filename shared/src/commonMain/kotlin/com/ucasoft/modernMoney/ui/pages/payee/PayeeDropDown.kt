package com.ucasoft.modernMoney.ui.pages.payee

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.model.Payee
import com.ucasoft.modernMoney.ui.components.EntityDropDown
import com.ucasoft.modernMoney.viewModels.payee.PayeesViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayeeDropDown(current: Payee?, label: String = "Payee", isEmptyAllowed: Boolean = true, onPayeeSelected: (Payee?) -> Unit) {

    val viewModel = koinViewModel<PayeesViewModel>()
    val state by viewModel.listState.collectAsStateWithLifecycle()

    EntityDropDown(
        current, label
    ) { expanded, onDismiss ->
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismiss() }
        ) {
            if (isEmptyAllowed) {
                DropdownMenuItem(
                    text = { Text("") },
                    onClick = {
                        onPayeeSelected(null)
                        onDismiss()
                    }
                )
            }
            state.items.forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {
                        onPayeeSelected(it)
                        onDismiss()
                    },
                    leadingIcon = it.logo?.let {
                        {
                            Image(
                                it,
                                "",
                                modifier = Modifier.size(24.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                )
            }
        }
    }
}