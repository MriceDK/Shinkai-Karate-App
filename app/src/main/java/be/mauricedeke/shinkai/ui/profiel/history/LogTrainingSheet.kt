package be.mauricedeke.shinkai.ui.profiel.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogTrainingSheet(
    date: LocalDate,
    initialType: String = "",
    initialStartTime: String = "",
    initialEndTime: String = "",
    onSave: (type: String, startTime: String, endTime: String, sensei: String, injuries: String) -> Unit,
    onDismiss: () -> Unit
) {
    val defaultStart = initialStartTime.ifBlank { "19:00" }
    val defaultEnd = initialEndTime.ifBlank { "21:00" }

    var type by remember { mutableStateOf(initialType) }
    var startTime by remember { mutableStateOf(defaultStart) }
    var endTime by remember { mutableStateOf(defaultEnd) }
    var sensei by remember { mutableStateOf("") }
    var injuries by remember { mutableStateOf("") }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val startHour = defaultStart.substringBefore(":").toIntOrNull() ?: 19
    val startMinute = defaultStart.substringAfter(":").toIntOrNull() ?: 0
    val endHour = defaultEnd.substringBefore(":").toIntOrNull() ?: 21
    val endMinute = defaultEnd.substringAfter(":").toIntOrNull() ?: 0

    val startPickerState = rememberTimePickerState(initialHour = startHour, initialMinute = startMinute, is24Hour = true)
    val endPickerState = rememberTimePickerState(initialHour = endHour, initialMinute = endMinute, is24Hour = true)

    if (showStartPicker) {
        AlertDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    startTime = "%02d:%02d".format(startPickerState.hour, startPickerState.minute)
                    showStartPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showStartPicker = false }) { Text("Annuleren") }
            },
            title = { Text("Starttijd") },
            text = { TimePicker(state = startPickerState) }
        )
    }

    if (showEndPicker) {
        AlertDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    endTime = "%02d:%02d".format(endPickerState.hour, endPickerState.minute)
                    showEndPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showEndPicker = false }) { Text("Annuleren") }
            },
            title = { Text("Eindtijd") },
            text = { TimePicker(state = endPickerState) }
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Training loggen",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                date.format(DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy", Locale("nl"))),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(4.dp))

            OutlinedTextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Type") },
                placeholder = { Text("bv. Kumite") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Van") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(modifier = Modifier.matchParentSize().clickable { showStartPicker = true })
                }
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tot") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(modifier = Modifier.matchParentSize().clickable { showEndPicker = true })
                }
            }

            OutlinedTextField(
                value = sensei,
                onValueChange = { sensei = it },
                label = { Text("Sensei") },
                placeholder = { Text("Sensei Ludo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = injuries,
                onValueChange = { injuries = it },
                label = { Text("Blessures") },
                placeholder = { Text("Geen") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(4.dp))
            Button(
                onClick = { onSave(type, startTime, endTime, sensei, injuries) },
                enabled = type.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Opslaan", fontWeight = FontWeight.Bold)
            }
        }
    }
}
