package com.ucasoft.modernMoney.ui.pages.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ucasoft.modern_money.shared.generated.resources.Res
import com.ucasoft.modern_money.shared.generated.resources.title
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen() {
    Card(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ListItem(
                headlineContent = { Text(stringResource(Res.string.title)) },
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Preferences()
            }
        }
    }
}

