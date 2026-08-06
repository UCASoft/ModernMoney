package com.ucasoft.modernMoney.ui.pages.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucasoft.modernMoney.db.filters.TransactionFilter
import com.ucasoft.modernMoney.db.repositories.AccountRepository
import com.ucasoft.modernMoney.db.repositories.CategoryRepository
import com.ucasoft.modernMoney.ui.pages.account.AccountCurrencyDropDown
import com.ucasoft.modernMoney.ui.pages.account.AccountDropDown
import com.ucasoft.modernMoney.ui.pages.account.CurrencyDropDown
import com.ucasoft.modernMoney.ui.pages.categories.CategoryDropDown
import org.koin.compose.koinInject

@Composable
fun TransactionFilterDialog(
    currentFilter: TransactionFilter,
    updateFilter: (TransactionFilter.() -> TransactionFilter) -> Unit
) {

    val accountsRepository = koinInject<AccountRepository>()
    val accounts by accountsRepository.accounts.collectAsStateWithLifecycle()
    val selectedAccount = currentFilter.accountId?.let { accounts[it] }
    val onlyAccountCurrencies = currentFilter.onlyAccountCurrencies

    val categoryRepository = koinInject<CategoryRepository>()

    val flatCategories by categoryRepository.flatCategories.collectAsStateWithLifecycle()
    val selectedCategory = currentFilter.categoryId?.let { flatCategories[it] }

    Column {
        AccountDropDown(selectedAccount) {
            updateFilter {
                copy(accountId = it?.id)
            }
        }
        Row(
            modifier = Modifier
                .toggleable(
                    onlyAccountCurrencies,
                    enabled = currentFilter.accountId != null,
                    onValueChange = {
                        updateFilter {
                            copy(onlyAccountCurrencies = it)
                        }
                    })
        ) {
            Checkbox(
                checked = onlyAccountCurrencies,
                null
            )
            Text("Only account currencies")
        }
        if (selectedAccount != null && onlyAccountCurrencies) {
            val selectedCurrency =
                selectedAccount.currencies.firstOrNull { it.currency.code == currentFilter.currencyCode }
            if (selectedCurrency == null) {
                updateFilter {
                    copy(currencyCode = null)
                }
            }
            AccountCurrencyDropDown(
                selectedAccount,
                selectedCurrency,
                isEmptyAllowed = true
            ) {
                updateFilter {
                    copy(currencyCode = it?.currency?.code)
                }
            }
        } else {
            CurrencyDropDown(
                currentFilter.currencyCode,
                emptyValue = ""
            ) {
                updateFilter {
                    copy(currencyCode = it?.code)
                }
            }
        }
        CategoryDropDown(selectedCategory) {
            updateFilter {
                copy(categoryId = it?.id)
            }
        }
        val isIncludeChildren = currentFilter.includeChildren && currentFilter.categoryId != null
        Row(
            modifier = Modifier
                .toggleable(
                    isIncludeChildren,
                    enabled = currentFilter.categoryId != null,
                    onValueChange = {
                        updateFilter {
                            copy(includeChildren = it)
                        }
                    })
        ) {
            Checkbox(
                checked = isIncludeChildren,
                null
            )
            Text("Include children categories")
        }
    }
}