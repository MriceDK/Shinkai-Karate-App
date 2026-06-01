package be.mauricedeke.shinkai.ui.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.ui.components.ShinkaiCalendar
import be.mauricedeke.shinkai.ui.theme.ShinkaiRed
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import java.time.LocalDate

@Composable
fun EventsScreen(
    uiState: EventsUiState,
    onDateSelected: (LocalDate) -> Unit = {},
    onEventClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val events = uiState.upcomingEvents.ifEmpty {
        listOf(Event("1", "Stage Naigairyu", "10:00", "16:00", "14/03", "Gent"),
               Event("2", "Stage Naigairyu", "10:00", "16:00", "14/03", "Gent"))
    }
    val inboxEvents = uiState.inboxEvents.ifEmpty {
        listOf(Event("3", "Stage test1234", "9:00", "12:00", "16 maart 2026", "Limburg", isInbox = true))
    }

    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState())) {
        Spacer(Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(horizontal = 16.dp, vertical = 8.dp)) {
            events.forEach { event ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.width(110.dp)) {
                        Text("${event.startTime} - ${event.endTime}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                        Text(event.date, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    }
                    Box(modifier = Modifier.width(1.dp).height(36.dp).background(MaterialTheme.colorScheme.outlineVariant))
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(event.title, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                        Text(event.location, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    }
                    IconButton(onClick = {}) { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text("Inbox ( ${inboxEvents.size} )", modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
            inboxEvents.firstOrNull()?.let { event ->
                Column(modifier = Modifier.fillMaxWidth().background(ShinkaiRed, RoundedCornerShape(16.dp)).padding(16.dp)) {
                    Text(event.title, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("${event.date} | ${event.startTime} - ${event.endTime}", color = MaterialTheme.colorScheme.onPrimary, fontSize = 13.sp)
                    Text(event.location, color = MaterialTheme.colorScheme.onPrimary, fontSize = 13.sp)
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = {}, colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary)) { Text("Ik kan niet") }
                        Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary, contentColor = ShinkaiRed)) { Text("Ik kan", fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary)) {
            Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary).padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text("Mon, Aug 17", color = MaterialTheme.colorScheme.onPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            ShinkaiCalendar(
                modifier = Modifier.padding(horizontal = 8.dp),
                initialMonth = LocalDate.of(2025, 8, 1),
                selectedDate = uiState.selectedDate,
                today = LocalDate.of(2025, 8, 5),
                onDateSelected = onDateSelected
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EventsScreenPreview() {
    ShinkaikarateappTheme { EventsScreen(uiState = EventsUiState()) }
}
