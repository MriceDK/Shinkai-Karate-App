package be.mauricedeke.shinkai.data.messaging

import be.mauricedeke.shinkai.domain.model.NotificationSettings
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotificationFilterTest {

    private val allEnabled = NotificationSettings(
        eventNotifications = true,
        trainingNotifications = true,
        examNotifications = true,
        changeNotifications = true,
        updateNotifications = true,
    )

    private val allDisabled = NotificationSettings(
        eventNotifications = false,
        trainingNotifications = false,
        examNotifications = false,
        changeNotifications = false,
        updateNotifications = false,
    )

    @Test
    fun event_enabledInSettings_returnsTrue() {
        assertTrue(isNotificationEnabled("event", allEnabled))
    }

    @Test
    fun training_enabledInSettings_returnsTrue() {
        assertTrue(isNotificationEnabled("training", allEnabled))
    }

    @Test
    fun exam_enabledInSettings_returnsTrue() {
        assertTrue(isNotificationEnabled("exam", allEnabled))
    }

    @Test
    fun change_enabledInSettings_returnsTrue() {
        assertTrue(isNotificationEnabled("change", allEnabled))
    }

    @Test
    fun update_enabledInSettings_returnsTrue() {
        assertTrue(isNotificationEnabled("update", allEnabled))
    }

    @Test
    fun event_disabledInSettings_returnsFalse() {
        assertFalse(isNotificationEnabled("event", allDisabled))
    }

    @Test
    fun training_disabledInSettings_returnsFalse() {
        assertFalse(isNotificationEnabled("training", allDisabled))
    }

    @Test
    fun exam_disabledInSettings_returnsFalse() {
        assertFalse(isNotificationEnabled("exam", allDisabled))
    }

    @Test
    fun change_disabledInSettings_returnsFalse() {
        assertFalse(isNotificationEnabled("change", allDisabled))
    }

    @Test
    fun update_disabledInSettings_returnsFalse() {
        assertFalse(isNotificationEnabled("update", allDisabled))
    }

    @Test
    fun unknownType_alwaysReturnsFalse() {
        assertFalse(isNotificationEnabled("event-reminder", allEnabled))
        assertFalse(isNotificationEnabled("training-reminder", allEnabled))
        assertFalse(isNotificationEnabled("", allEnabled))
        assertFalse(isNotificationEnabled("unknown", allEnabled))
    }

    @Test
    fun individualToggles_onlyAffectTheirOwnType() {
        val settings = NotificationSettings(
            eventNotifications = true,
            trainingNotifications = false,
            examNotifications = true,
            changeNotifications = false,
            updateNotifications = true,
        )
        assertTrue(isNotificationEnabled("event", settings))
        assertFalse(isNotificationEnabled("training", settings))
        assertTrue(isNotificationEnabled("exam", settings))
        assertFalse(isNotificationEnabled("change", settings))
        assertTrue(isNotificationEnabled("update", settings))
    }
}
