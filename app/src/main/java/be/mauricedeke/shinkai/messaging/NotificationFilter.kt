package be.mauricedeke.shinkai.messaging

import be.mauricedeke.shinkai.domain.model.NotificationSettings

internal fun isNotificationEnabled(type: String, settings: NotificationSettings): Boolean =
    when (type) {
        "event" -> settings.eventNotifications
        "training" -> settings.trainingNotifications
        "exam" -> settings.examNotifications
        "change" -> settings.changeNotifications
        "update" -> settings.updateNotifications
        else -> false
    }
