package be.mauricedeke.shinkai.ui.home

import androidx.annotation.DrawableRes
import be.mauricedeke.shinkai.R
import be.mauricedeke.shinkai.ui.navigation.Screen

enum class ShortcutId(val label: String, @DrawableRes val iconRes: Int, val route: String) {
    EVENTS("Events", R.drawable.ic_event, Screen.Events.route),
    KAART("Kaart", R.drawable.ic_map, Screen.Kaart.route),
    LEXICON("Lexicon", R.drawable.ic_menu_book, Screen.Lexicon.route),
    TECHNIEKEN("Technieken", R.drawable.ic_directions_walk, Screen.Technieken.route),
    PROFIEL("Profiel", R.drawable.ic_account_circle, Screen.Profiel.route),
    NOTIFICATIONS("Meldingen", R.drawable.ic_notifications, Screen.Notifications.route),
    STRENGTH_TEST("Kracht", R.drawable.ic_fitness_center, Screen.StrengthTest.route),
    TRAINING_HISTORY("Training", R.drawable.ic_history, Screen.TrainingHistory.route),
}

val defaultShortcuts = listOf(
    ShortcutId.EVENTS,
    ShortcutId.KAART,
    ShortcutId.LEXICON,
    ShortcutId.TECHNIEKEN
)

const val MAX_SHORTCUTS = 4
