package be.mauricedeke.shinkai.ui.navigation

import androidx.annotation.DrawableRes
import be.mauricedeke.shinkai.R

sealed class Screen(
    val route: String,
    val label: String = "",
    @DrawableRes val iconRes: Int = R.drawable.ic_pan_tool
) {
    data object Home : Screen("home", "Home", R.drawable.ic_pan_tool)
    data object Technieken : Screen("technieken", "Technieken", R.drawable.ic_directions_walk)
    data object TechniekDetail : Screen("technieken/{belt}") {
        fun createRoute(belt: String) = "technieken/$belt"
    }

    data object Events : Screen("events", "Events", R.drawable.ic_event)
    data object EventDetail : Screen("events/{eventId}") {
        fun createRoute(id: String) = "events/$id"
    }

    data object ManageEvents : Screen("events/manage")

    data object Lexicon : Screen("lexicon", "Lexicon", R.drawable.ic_menu_book)
    data object Profiel : Screen("profiel", "Profiel", R.drawable.ic_account_circle)
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
