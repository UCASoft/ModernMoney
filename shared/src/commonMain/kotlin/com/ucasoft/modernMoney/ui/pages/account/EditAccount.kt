package com.ucasoft.modernMoney.ui.pages.account

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ucasoft.modernMoney.model.Account
import com.ucasoft.modernMoney.ui.pages.bank.BankDropDown
import com.ucasoft.modernMoney.viewModels.account.AccountViewModel

@Composable
fun EditAccount(account: Account?, viewModel: AccountViewModel) {
    Column {
        OutlinedTextField(
            value = account?.name ?: "",
            onValueChange = {
                viewModel.updateAccountName(it)
            },
            label = { Text("Account Name") }
        )
        BankDropDown(account?.bank) {
            viewModel.updateAccountBank(it)
        }
    }
}