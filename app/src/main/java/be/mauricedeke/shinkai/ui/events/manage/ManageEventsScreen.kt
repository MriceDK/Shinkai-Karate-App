package be.mauricedeke.shinkai.ui.events.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.ui.components.RoundBackButton
import java.time.LocalDate

@Composable
fun ManageEventsScreen(
    uiState: ManageEventsUiState,
    onRsvp: (eventId: String, attending: Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(48.dp))
            Text(
                "Beheer Events",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Beheer je aanwezigheid voor alle geplande events.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(24.dp))

            if (uiState.isError) {
                Spacer(Modifier.height(48.dp))
                Text(
                    "Kon events niet laden. Probeer opnieuw.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else if (uiState.events.isEmpty()) {
                Spacer(Modifier.height(48.dp))
                Text(
                    "Geen events gevonden.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                uiState.events.forEachIndexed { index, event ->
                    if (index > 0) HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    EventManageRow(
                        event = event,
                        rsvp = uiState.rsvp[event.id],
                        onRsvp = { attending -> onRsvp(event.id, attending) }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }

        RoundBackButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventManageRow(
    event: Event,
    rsvp: Boolean?,
    onRsvp: (Boolean) -> Unit
) {
    val isUpcoming = event.localDate == null || !event.localDate.isBefore(LocalDate.now())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    event.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = if (isUpcoming) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "${event.date}  •  ${event.startTime} – ${event.endTime}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (event.city.isNotBlank()) {
                    Text(event.city, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (!isUpcoming) {
                Text(
                    "Voorbij",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = rsvp == false,
                onClick = { onRsvp(false) },
                enabled = isUpcoming,
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    activeContentColor = MaterialTheme.colorScheme.primary,
                    activeBorderColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Text("Ik kan niet", fontSize = 13.sp)
            }
            SegmentedButton(
                selected = rsvp == true,
                onClick = { onRsvp(true) },
                enabled = isUpcoming,
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.primary,
                    activeContentColor = Color.White,
                    activeBorderColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Text("Ik kan", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}
