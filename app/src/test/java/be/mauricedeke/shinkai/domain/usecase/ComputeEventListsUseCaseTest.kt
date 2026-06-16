package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.Event
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.util.UUID

class ComputeEventListsUseCaseTest {

    private val useCase = ComputeEventListsUseCase()
    private val today: LocalDate = LocalDate.now()

    @Test
    fun invoke_emptyEvents_returnsEmptyLists() {
        val result = useCase(emptyList(), emptyMap())
        assertTrue(result.upcomingEvents.isEmpty())
        assertTrue(result.allUpcomingEvents.isEmpty())
        assertTrue(result.allEvents.isEmpty())
        assertTrue(result.inboxEvents.isEmpty())
    }

    @Test
    fun invoke_pastEvent_isFilteredFromUpcomingButInAllEvents() {
        val past = Event(localDate = today.minusDays(1), title = "Past event")
        val result = useCase(listOf(past), emptyMap())
        assertTrue(result.upcomingEvents.isEmpty())
        assertTrue(result.allUpcomingEvents.isEmpty())
        assertEquals(1, result.allEvents.size)
    }

    @Test
    fun invoke_todayEvent_isKept() {
        val event = Event(localDate = today, title = "Today")
        val result = useCase(listOf(event), emptyMap())
        assertEquals(1, result.upcomingEvents.size)
    }

    @Test
    fun invoke_futureEvent_isKept() {
        val event = Event(localDate = today.plusDays(1), title = "Tomorrow")
        val result = useCase(listOf(event), emptyMap())
        assertEquals(1, result.upcomingEvents.size)
    }

    @Test
    fun invoke_nullLocalDate_isKept() {
        val event = Event(localDate = null, title = "No date")
        val result = useCase(listOf(event), emptyMap())
        assertEquals(1, result.upcomingEvents.size)
    }

    @Test
    fun invoke_upcomingEvents_limitedToFour() {
        val events = (1..6).map { Event(localDate = today.plusDays(it.toLong()), title = "Event $it") }
        val result = useCase(events, emptyMap())
        assertEquals(4, result.upcomingEvents.size)
    }

    @Test
    fun invoke_allUpcomingEvents_notLimited() {
        val events = (1..6).map { Event(localDate = today.plusDays(it.toLong()), title = "Event $it") }
        val result = useCase(events, emptyMap())
        assertEquals(6, result.allUpcomingEvents.size)
    }

    @Test
    fun invoke_sortsByDateAscending() {
        val later = Event(localDate = today.plusDays(3), title = "Later")
        val sooner = Event(localDate = today.plusDays(1), title = "Sooner")
        val result = useCase(listOf(later, sooner), emptyMap())
        assertEquals("Sooner", result.allUpcomingEvents[0].title)
        assertEquals("Later", result.allUpcomingEvents[1].title)
    }

    @Test
    fun invoke_nullDateSortedLast() {
        val noDate = Event(localDate = null, title = "No date")
        val withDate = Event(localDate = today.plusDays(1), title = "Has date")
        val result = useCase(listOf(noDate, withDate), emptyMap())
        assertEquals("Has date", result.allUpcomingEvents[0].title)
        assertEquals("No date", result.allUpcomingEvents[1].title)
    }

    @Test
    fun invoke_noRsvp_appearsInInbox() {
        val id = UUID.randomUUID()
        val event = Event(id = id, localDate = today.plusDays(1))
        val result = useCase(listOf(event), emptyMap())
        assertEquals(1, result.inboxEvents.size)
    }

    @Test
    fun invoke_confirmedRsvp_notInInbox() {
        val id = UUID.randomUUID()
        val event = Event(id = id, localDate = today.plusDays(1))
        val result = useCase(listOf(event), mapOf(id to true))
        assertTrue(result.inboxEvents.isEmpty())
    }

    @Test
    fun invoke_declinedRsvp_notInInbox() {
        val id = UUID.randomUUID()
        val event = Event(id = id, localDate = today.plusDays(1))
        val result = useCase(listOf(event), mapOf(id to false))
        assertTrue(result.inboxEvents.isEmpty())
    }

    @Test
    fun invoke_mixedPastAndFuture_onlyFutureInUpcoming() {
        val past = Event(localDate = today.minusDays(1), title = "Past")
        val future = Event(localDate = today.plusDays(1), title = "Future")
        val result = useCase(listOf(past, future), emptyMap())
        assertEquals(1, result.allUpcomingEvents.size)
        assertEquals("Future", result.allUpcomingEvents[0].title)
        assertEquals(2, result.allEvents.size)
    }
}
