package be.mauricedeke.shinkai.ui.profiel.notifications

import be.mauricedeke.shinkai.domain.model.NotificationSettings

data class NotificationsUiState(
    val settings: NotificationSettings = NotificationSettings()
)
