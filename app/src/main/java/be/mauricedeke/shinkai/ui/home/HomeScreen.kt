package be.mauricedeke.shinkai.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.model.Training
import be.mauricedeke.shinkai.ui.navigation.Screen
import be.mauricedeke.shinkai.ui.theme.ShinkaiCardBg
import be.mauricedeke.shinkai.ui.theme.ShinkaiRed
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import java.time.LocalDate

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit = {},
    onEventClick: (String) -> Unit = {},
) {
    val fallbackEvents = if (uiState.upcomingEvents.isEmpty())
        listOf(
            Event("1", "Stage Naigairyu", "10:00", "16:00", "14/03", "Kapellestraat 79", "Evergem"),
            Event("2", "Stage Naigairyu", "10:00", "16:00", "14/03", "Kapellestraat 79", "Evergem"),
        )
    else uiState.upcomingEvents

    val fallbackTraining = uiState.nextTraining ?: Training(
        id = "1", type = "Technieken",
        startTime = "20:00", endTime = "22:00",
        date = LocalDate.of(2025, 3, 18)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))

        EventSection(title = "UPCOMING EVENTS") {
            fallbackEvents.forEach { event ->
                EventRow("${event.startTime} - ${event.endTime}", event.date, event.title, event.location, onClick = { onEventClick(event.id) })
            }
        }

        Spacer(Modifier.height(8.dp))

        EventSection(title = "VOLGENDE TRAINING") {
            EventRow(
                "${fallbackTraining.startTime} - ${fallbackTraining.endTime}",
                fallbackTraining.date.toString(),
                fallbackTraining.type,
                ""
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("SHORTCUTS", fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline, fontSize = 16.sp)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = ShinkaiRed),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.height(32.dp)
            ) { Text("Edit", fontSize = 13.sp) }
        }

        Spacer(Modifier.height(8.dp))

        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                ShortcutCard("Events", Icons.Default.Event, Modifier.weight(1f)) { onNavigate(Screen.Events.route) }
                Spacer(Modifier.width(8.dp))
                ShortcutCard("Kaart", Icons.Default.Map, Modifier.weight(1f)) { onNavigate(Screen.Kaart.route) }
            }
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                ShortcutCard("Lexicon", Icons.AutoMirrored.Filled.MenuBook, Modifier.weight(1f)) { onNavigate(Screen.Lexicon.route) }
                Spacer(Modifier.width(8.dp))
                ShortcutCard("Technieken", Icons.AutoMirrored.Filled.DirectionsWalk, Modifier.weight(1f)) { onNavigate(Screen.Technieken.route) }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun EventSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(16.dp)) {
        Text(title, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline,
            modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun EventRow(time: String, date: String, name: String, location: String, onClick: (() -> Unit)? = null) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).then(if (onClick != null) Modifier.clickable { onClick() } else Modifier), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.width(110.dp)) {
            Text(time, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(date, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
        Box(modifier = Modifier.width(1.dp).height(36.dp).background(MaterialTheme.colorScheme.outlineVariant))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(name, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
            if (location.isNotBlank()) Text(location, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
    }
}

@Composable
private fun ShortcutCard(label: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier.height(140.dp).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(8.dp))
            Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    ShinkaikarateappTheme {
        HomeScreen(uiState = HomeUiState())
    }
}
