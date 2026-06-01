package be.mauricedeke.shinkai.ui.profiel.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import be.mauricedeke.shinkai.ui.components.RoundBackButton
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.Training
import be.mauricedeke.shinkai.ui.components.ShinkaiCalendar
import be.mauricedeke.shinkai.ui.theme.ShinkaiRed
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import java.time.LocalDate

@Composable
fun TrainingHistoryScreen(
    uiState: TrainingHistoryUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {},
) {
    val displayTrainings = uiState.selectedTrainings.ifEmpty {
        listOf(
            Training("1", "Technieken", "20:00", "22:00", LocalDate.of(2025, 8, 14), "Geen", "Geen"),
            Training("2", "Technieken", "20:00", "22:00", LocalDate.of(2025, 8, 14), "Geen", "Geen"),
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("TRAINING LOG", fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline, fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterHorizontally), color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(12.dp))
        ShinkaiCalendar(
            initialMonth = LocalDate.of(2025, 8, 1),
            selectedDate = uiState.selectedDate,
            today = LocalDate.of(2025, 8, 5),
            onDateSelected = onDateSelected
        )
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("TRAINING DATA", fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                Text("Maandag ${uiState.selectedDate?.toString() ?: "14/08/2025"}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.weight(1f))
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = ShinkaiRed), shape = RoundedCornerShape(4.dp), modifier = Modifier.height(32.dp), contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp)) {
                Text("Edit", fontSize = 13.sp)
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            displayTrainings.take(2).forEach { training ->
                Column(modifier = Modifier.weight(1f).padding(4.dp)) {
                    listOf("TYPE :" to training.type, "DUUR :" to "${training.startTime} - ${training.endTime}", "BLESSURES :" to training.injuries, "SENSEI :" to training.sensei)
                        .forEach { (label, value) ->
                            Row(modifier = Modifier.padding(vertical = 3.dp)) {
                                Text(label, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline, fontSize = 12.sp, modifier = Modifier.padding(end = 4.dp), color = MaterialTheme.colorScheme.onSurface)
                                Text(value, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
    RoundBackButton(
        onClick = onBackClick,
        modifier = Modifier.align(Alignment.TopStart).padding(start = 16.dp, top = 8.dp)
    )
}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TrainingHistoryScreenPreview() {
    ShinkaikarateappTheme { TrainingHistoryScreen(uiState = TrainingHistoryUiState()) }
}
