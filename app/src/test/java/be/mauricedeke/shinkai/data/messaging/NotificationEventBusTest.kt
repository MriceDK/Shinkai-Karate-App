package be.mauricedeke.shinkai.data.messaging

import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class NotificationEventBusTest {

    private val bus = NotificationEventBus()

    @Test
    fun emit_deliversNotificationToCollector() = runTest {
        val collected = mutableListOf<InAppNotification>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            bus.events.collect { collected.add(it) }
        }

        val notification = InAppNotification("training", "Class Tonight", "Training at 7pm")
        bus.emit(notification)

        assertEquals(1, collected.size)
        assertEquals(notification, collected.first())
        job.cancel()
    }

    @Test
    fun emit_deliversToMultipleCollectors() = runTest {
        val first = mutableListOf<InAppNotification>()
        val second = mutableListOf<InAppNotification>()
        val job1 = launch(UnconfinedTestDispatcher(testScheduler)) { bus.events.collect { first.add(it) } }
        val job2 = launch(UnconfinedTestDispatcher(testScheduler)) { bus.events.collect { second.add(it) } }

        val notification = InAppNotification("event", "Tournament", "Sign up now")
        bus.emit(notification)

        assertEquals(notification, first.first())
        assertEquals(notification, second.first())
        job1.cancel()
        job2.cancel()
    }

    @Test
    fun emit_multipleNotifications_allDeliveredInOrder() = runTest {
        val collected = mutableListOf<InAppNotification>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            bus.events.collect { collected.add(it) }
        }

        val first = InAppNotification("event", "First", "Body 1")
        val second = InAppNotification("training", "Second", "Body 2")
        bus.emit(first)
        bus.emit(second)

        assertEquals(2, collected.size)
        assertEquals(first, collected[0])
        assertEquals(second, collected[1])
        job.cancel()
    }

    @Test
    fun inAppNotification_holdsCorrectFields() {
        val notification = InAppNotification("exam", "Belt Test", "Next Friday")
        assertEquals("exam", notification.type)
        assertEquals("Belt Test", notification.title)
        assertEquals("Next Friday", notification.body)
    }

    @Test
    fun inAppNotification_equalityBasedOnAllFields() {
        val a = InAppNotification("event", "Title", "Body")
        val b = InAppNotification("event", "Title", "Body")
        val c = InAppNotification("training", "Title", "Body")
        assertEquals(a, b)
        assertNotEquals(a, c)
    }
}
