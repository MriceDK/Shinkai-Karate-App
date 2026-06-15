package be.mauricedeke.shinkai.ui.profiel

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.R
import be.mauricedeke.shinkai.ui.components.SectionHeader
import be.mauricedeke.shinkai.ui.components.SettingsListItem
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import be.mauricedeke.shinkai.ui.theme.toBeltColor
import be.mauricedeke.shinkai.ui.theme.toColor
import coil.compose.AsyncImage

@Composable
fun ProfielScreen(
    uiState: ProfielUiState,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onKaartClick: () -> Unit = {},
    onStrengthTestClick: () -> Unit = {},
    onTrainingHistoryClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLocationClick: () -> Unit = {},
    onSupportClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    isDarkTheme: Boolean = false,
    onDarkThemeToggle: (Boolean) -> Unit = {},
) {
    val profile = uiState.userProfile
    val loggedIn = profile != null
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (profile?.profilePictureUri != null) {
                    AsyncImage(
                        model = profile.profilePictureUri,
                        contentDescription = "Profile picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(if (loggedIn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            null,
                            tint = if (loggedIn) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        if (loggedIn) profile!!.name.ifBlank { "Onbekende gebruiker" } else "Niet ingelogd",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (loggedIn) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        if (loggedIn) profile!!.email.ifBlank { "" } else "Log in om je profiel te bekijken",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    Button(
                        onClick = if (loggedIn) onEditClick else onLoginClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.height(28.dp),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp)
                    ) {
                        Text(if (loggedIn) "Edit" else "Log in", fontSize = 12.sp)
                    }
                }
            }
            if (loggedIn) {
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val beltName = profile!!.belt.ifBlank { "Geel" }
                    val beltColor = beltName.toBeltColor().toColor(isDark = isDarkTheme)
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(width = 48.dp, height = 16.dp)
                                .clip(RoundedCornerShape(50))
                                .background(beltColor)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        beltName,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SettingsListItem(
                painterResource(R.drawable.ic_fitness_center),
                "Strength Test",
                onClick = onStrengthTestClick
            )
            SettingsListItem(
                painterResource(R.drawable.ic_calendar_month),
                "Training History",
                onClick = onTrainingHistoryClick
            )
            SettingsListItem(
                painterResource(R.drawable.ic_map),
                "Kaart",
                onClick = onKaartClick
            )
        }
        Spacer(Modifier.height(8.dp))
        SectionHeader("Privacy Settings")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SettingsListItem(
                painterResource(R.drawable.ic_notifications),
                "Notifications",
                onClick = onNotificationsClick
            )
            SettingsListItem(
                painterResource(R.drawable.ic_location_on),
                "Location",
                onClick = onLocationClick
            )
        }
        Spacer(Modifier.height(8.dp))
        SectionHeader("General Settings")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ProfileToggleItem(
                painter = painterResource(R.drawable.ic_wb_sunny),
                checked = isDarkTheme,
                onCheckedChange = onDarkThemeToggle
            )
            SettingsListItem(
                painterResource(R.drawable.ic_chat),
                "Contact Support",
                onClick = onSupportClick
            )
        }
        Spacer(Modifier.height(8.dp))
        SectionHeader("Socials")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SettingsListItem(
                painterResource(R.drawable.ic_link),
                "Shinkai.be",
                labelColor = Color(0xFF1565C0),
                onClick = { uriHandler.openUri("https://www.shinkai.be") })
        }
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (loggedIn) {
                SettingsListItem(
                    painterResource(R.drawable.ic_exit_to_app),
                    "Log out",
                    onClick = onLogoutClick
                )
            } else {
                SettingsListItem(
                    painterResource(R.drawable.ic_login),
                    "Log in",
                    onClick = onLoginClick
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}


@Composable
private fun ProfileToggleItem(
    painter: Painter,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter,
            null,
            modifier = Modifier.size(22.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.width(16.dp))
        Text(
            "Dark mode",
            modifier = Modifier.weight(1f),
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}


@Preview(name = "Not logged in", showBackground = true, showSystemUi = true)
@Composable
fun ProfielScreenNotLoggedInPreview() {
    ShinkaikarateappTheme { ProfielScreen(uiState = ProfielUiState()) }
}

@Preview(name = "With profile data", showBackground = true, showSystemUi = true)
@Composable
fun ProfielScreenWithProfilePreview() {
    ShinkaikarateappTheme {
        ProfielScreen(
            uiState = ProfielUiState(
                userProfile = be.mauricedeke.shinkai.domain.model.UserProfile(
                    name = "Maurice De Kegel",
                    email = "maurice@shinkai.be",
                    belt = "Oranje"
                )
            )
        )
    }
}

@Preview(
    name = "Dark — with profile",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ProfielScreenDarkPreview() {
    ShinkaikarateappTheme(darkTheme = true) {
        ProfielScreen(
            uiState = ProfielUiState(
                userProfile = be.mauricedeke.shinkai.domain.model.UserProfile(
                    name = "Maurice De Kegel",
                    email = "maurice@shinkai.be",
                    belt = "Oranje"
                )
            ),
            isDarkTheme = true
        )
    }
}
