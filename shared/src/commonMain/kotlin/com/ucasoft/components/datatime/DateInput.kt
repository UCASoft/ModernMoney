package com.ucasoft.components.datatime

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInput(value: Instant?, modifier: Modifier = Modifier, wholeDay: Boolean = false, onValueChange: (Instant) -> Unit) {
    var showDataPicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(value?.toEpochMilliseconds() ?: Clock.System.now().toEpochMilliseconds())

    Box(modifier = modifier) {
        OutlinedTextField(
            value = value?.format(DateTimeComponents.Format {
                year(); char('-'); monthNumber(); char('-'); day()
            }) ?: "",
            onValueChange = {},
            label = { Text("Date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    showDataPicker = true
                }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            }
        )
    }

    if (showDataPicker) {
        DatePickerDialog(
            onDismissRequest = { showDataPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    if (selectedDate != null) {
                        onValueChange(Instant.fromEpochMilliseconds(selectedDate).let {
                            if (wholeDay) {
                                it.plus(1.days).minus(1.seconds)
                            } else {
                                it
                            }
                        })
                    }
                    showDataPicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDataPicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}