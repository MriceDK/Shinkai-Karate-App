package be.mauricedeke.shinkai.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import be.mauricedeke.shinkai.ui.navigation.Screen

enum class ShortcutId(val label: String, val icon: ImageVector, val route: String) {
    EVENTS("Events", Icons.Default.Event, Screen.Events.route),
    KAART("Kaart", Icons.Default.Map, Screen.Kaart.route),
    LEXICON("Lexicon", Icons.AutoMirrored.Filled.MenuBook, Screen.Lexicon.route),
    TECHNIEKEN("Technieken", Icons.AutoMirrored.Filled.DirectionsWalk, Screen.Technieken.route),
    PROFIEL("Profiel", Icons.Default.AccountCircle, Screen.Profiel.route),
    NOTIFICATIONS("Meldingen", Icons.Default.Notifications, Screen.Notifications.route),
    STRENGTH_TEST("Kracht", Icons.Default.FitnessCenter, Screen.StrengthTest.route),
    TRAINING_HISTORY("Training", Icons.Default.History, Screen.TrainingHistory.route),
}

val defaultShortcuts = listOf(
    ShortcutId.EVENTS,
    ShortcutId.KAART,
    ShortcutId.LEXICON,
    ShortcutId.TECHNIEKEN
)

const val MAX_SHORTCUTS = 4
