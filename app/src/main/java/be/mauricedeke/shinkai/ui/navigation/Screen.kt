package be.mauricedeke.shinkai.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val label: String = "",
    val icon: ImageVector = Icons.Default.PanTool
) {
    data object Home : Screen("home", "Home", Icons.Default.PanTool)
    data object Technieken : Screen("technieken", "Technieken", Icons.Default.DirectionsWalk)
    data object TechniekDetail : Screen("technieken/{belt}") {
        fun createRoute(belt: String) = "technieken/$belt"
    }

    data object Events : Screen("events", "Events", Icons.Default.Event)
    data object EventDetail : Screen("events/{eventId}") {
        fun createRoute(id: String) = "events/$id"
    }
    data object ManageEvents : Screen("events/manage")

    data object Lexicon : Screen("lexicon", "Lexicon", Icons.AutoMirrored.Filled.MenuBook)
    data object Profiel : Screen("profiel", "Profiel", Icons.Default.AccountCircle)
    data object Account : Screen("account")
    data object TrainingHistory : Screen("training_history")
    data object StrengthTest : Screen("strength_test")
    data object PunchTest : Screen("punch_test")
    data object KiaiTest : Screen("kiai_test")
    data object Notifications : Screen("notifications")
    data object Location : Screen("location")
    data object Kaart : Screen("kaart")
    data object KataList : Screen("technieken/katas")
    data object Login : Screen("login")
    data object Support : Screen("support")
    data object TrainingSessionDetail : Screen("training-session/{sessionId}") {
        fun createRoute(id: String) = "training-session/$id"
    }
}

val bottomNavScreens = listOf(
    Screen.Technieken,
    Screen.Events,
    Screen.Home,
    Screen.Lexicon,
    Screen.Profiel
)
