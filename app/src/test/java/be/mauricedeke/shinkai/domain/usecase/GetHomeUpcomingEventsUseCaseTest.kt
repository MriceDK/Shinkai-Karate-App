package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.Event
import be.mauricedeke.shinkai.domain.repository.EventRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.util.UUID

class GetHomeUpcomingEventsUseCaseTest {

    private val today: LocalDate = LocalDate.now()

    private fun makeUseCase(events: List<Event>?): GetHomeUpcomingEventsUseCase {
        val repo = object : EventRepository {
            override suspend fun getEvents() = events
            override suspend fun getEventById(id: UUID) = events?.find { it.id == id }
            override suspend fun setRsvp(id: UUID, attending: Boolean?) {}
        }
        return GetHomeUpcomingEventsUseCase(repo)
    }

    @Test
    fun invoke_repositoryReturnsNull_returnsNull() = runTest {
        assertNull(makeUseCase(null)())
    }

    @Test
    fun invoke_emptyList_returnsEmpty() = runTest {
        val result = makeUseCase(emptyList())()
        assertNotNull(result)
        assertTrue(result!!.isEmpty())
    }

    @Test
    fun invoke_filtersPastEvents() = runTest {
        val past = Event(localDate = today.minusDays(1), title = "Past")
        val result = makeUseCase(listOf(past))()
        assertNotNull(result)
        assertTrue(result!!.isEmpty())
    }

    @Test
    fun invoke_keepsTodayEvent() = runTest {
        val event = Event(localDate = today, title = "Today")
        val result = makeUseCase(listOf(event))()
        assertEquals(1, result?.size)
    }

    @Test
    fun invoke_keepsFutureEvent() = runTest {
        val event = Event(localDate = today.plusDays(5), title = "Future")
        val result = makeUseCase(listOf(event))()
        assertEquals(1, result?.size)
    }

    @Test
    fun invoke_nullLocalDate_isKept() = runTest {
        val event = Event(localDate = null, title = "No date")
        val result = makeUseCase(listOf(event))()
        assertEquals(1, result?.size)
    }

    @Test
    fun invoke_takesMaxTwo() = runTest {
        val events = (1..5).map { Event(localDate = today.plusDays(it.toLong())) }
        val result = makeUseCase(events)()
        assertEquals(2, result?.size)
    }

    @Test
    fun invoke_exactlyTwo_returnsBoth() = runTest {
        val events = listOf(
            Event(localDate = today.plusDays(1), title = "First"),
            Event(localDate = today.plusDays(2), title = "Second")
        )
        val result = makeUseCase(events)()
        assertEquals(2, result?.size)
    }

    @Test
    fun invoke_mixedPastAndFuture_onlyFutureReturned() = runTest {
        val past = Event(localDate = today.minusDays(1), title = "Past")
        val future = Event(localDate = today.plusDays(1), title = "Future")
        val result = makeUseCase(listOf(past, future))()
        assertEquals(1, result?.size)
        assertEquals("Future", result?.first()?.title)
    }
}
