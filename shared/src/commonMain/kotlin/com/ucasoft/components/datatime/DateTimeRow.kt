package com.ucasoft.components.datatime

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeRow(value: Instant, onValueChange: (Instant) -> Unit) {

    var showDataPicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(value.toEpochMilliseconds())

    var showTimePicker by remember { mutableStateOf(false) }
    val localDateTime = value.toLocalDateTime(TimeZone.currentSystemDefault())
    val timePickerState = rememberTimePickerState(localDateTime.hour, localDateTime.minute, true)


    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.weight(1f).padding(8.dp, 4.dp)
        ) {
            OutlinedTextField(
                value = value.format(DateTimeComponents.Format {
                    year(); char('-'); monthNumber(); char('-'); day()
                }),
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
        Box(
            modifier = Modifier.weight(1f).padding(8.dp, 4.dp)
        ) {
            OutlinedTextField(
                value = value.format(DateTimeComponents.Format {
                    hour(); char(':'); minute()
                }),
                onValueChange = {},
                label = { Text("Time") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {
                        showTimePicker = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Select time"
                        )
                    }
                }
            )
        }
    }
    if (showDataPicker) {
        DatePickerDialog(
            onDismissRequest = { showDataPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    if (selectedDate != null) {
                        onValueChange(value.withDate(selectedDate))
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

    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    onValueChange(value.withTime(timePickerState.hour, timePickerState.minute))
                    showTimePicker = false
                }) {
                    Text("OK")
                }
            },
            title = { Text("Select time") },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            TimePicker(
                state = timePickerState
            )
        }
    }
}

fun Instant.withDate(epochMilliseconds: Long) : Instant {
    val timeZone = TimeZone.currentSystemDefault()

    val localDateTime = this.toLocalDateTime(timeZone)

    val newDate = LocalDate.fromEpochDays(epochMilliseconds.milliseconds.inWholeDays.toInt())

    return LocalDateTime(newDate, localDateTime.time).toInstant(timeZone)
}

fun Instant.withTime(hour: Int, minute: Int) : Instant {
    val timeZone = TimeZone.currentSystemDefault()

    val localDateTime = this.toLocalDateTime(timeZone)

    val newTime = LocalTime(hour, minute)

    return LocalDateTime(localDateTime.date, newTime).toInstant(timeZone)
}