package be.mauricedeke.shinkai.ui.events.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.ui.components.RoundBackButton
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    uiState: EventDetailUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onRsvp: (Boolean) -> Unit = {}
) {
    val event = uiState.event
    val context = LocalContext.current
    val isUpcoming = event.localDate == null || !event.localDate.isBefore(LocalDate.now())

    fun openNavigation() {
        val query = Uri.encode("${event.location}, ${event.city}")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$query"))
        context.startActivity(intent)
    }

    Box(modifier = modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Text(
            event.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${event.startTime} - ${event.endTime}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    event.date,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .width(1.dp)
                    .height(36.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(
                    enabled = event.location.isNotBlank(),
                    onClick = ::openNavigation
                )
            ) {
                Column {
                    Text(
                        event.location,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                    Text(
                        event.city,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                }
                if (event.location.isNotBlank()) {
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        Icons.Default.Navigation,
                        contentDescription = "Navigeer",
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(50)
        ) {
            Text("+ Add to calendar", fontSize = 15.sp)
        }
        if (isUpcoming) {
            Spacer(Modifier.height(12.dp))
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth(0.85f)) {
                SegmentedButton(
                    selected = uiState.rsvp == false,
                    onClick = { onRsvp(false) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    icon = {
                        if (uiState.rsvp == false) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                            )
                        }
                    },
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        activeContentColor = MaterialTheme.colorScheme.primary,
                        activeBorderColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Text("Ik kan niet", fontSize = 13.sp)
                }
                SegmentedButton(
                    selected = uiState.rsvp == true,
                    onClick = { onRsvp(true) },
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
        Spacer(Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.FitnessCenter,
                null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "About Evenement",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Text(
            event.description.ifBlank { "Informatie over het evenement." },
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Text("Verwijder Evenement", fontSize = 15.sp)
        }
        Spacer(Modifier.height(16.dp))
    }
    RoundBackButton(
        onClick = onBackClick,
        modifier = Modifier
            .align(Alignment.TopStart)
            .padding(start = 16.dp, top = 8.dp)
    )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EventDetailScreenPreview() {
    ShinkaikarateappTheme { EventDetailScreen(uiState = EventDetailUiState()) }
}
