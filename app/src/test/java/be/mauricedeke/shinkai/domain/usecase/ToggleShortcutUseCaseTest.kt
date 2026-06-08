package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.LocationSettings
import be.mauricedeke.shinkai.domain.model.NotificationSettings
import be.mauricedeke.shinkai.domain.repository.SettingsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ToggleShortcutUseCaseTest {

    private lateinit var useCase: ToggleShortcutUseCase
    private val savedShortcuts = mutableListOf<String>()

    @Before
    fun setUp() {
        savedShortcuts.clear()
        val fakeRepo = object : SettingsRepository {
            override suspend fun getNotificationSettings() = NotificationSettings()
            override suspend fun updateNotificationSettings(settings: NotificationSettings) {}
            override suspend fun getLocationSettings() = LocationSettings()
            override suspend fun updateLocationSettings(settings: LocationSettings) {}
            override suspend fun getShortcuts() = savedShortcuts.toList()
            override suspend fun updateShortcuts(ids: List<String>) {
                savedShortcuts.clear()
                savedShortcuts.addAll(ids)
            }
        }
        useCase = ToggleShortcutUseCase(UpdateShortcutsUseCase(fakeRepo))
    }

    @Test
    fun invoke_newId_isAdded() = runTest {
        val result = useCase(listOf("EVENTS"), "KAART", 4)
        assertTrue(result.contains("KAART"))
        assertTrue(result.contains("EVENTS"))
    }

    @Test
    fun invoke_existingId_isRemoved() = runTest {
        val result = useCase(listOf("EVENTS", "KAART"), "EVENTS", 4)
        assertFalse(result.contains("EVENTS"))
        assertTrue(result.contains("KAART"))
    }

    @Test
    fun invoke_atMaxCapacity_newIdNotAdded() = runTest {
        val full = listOf("A", "B", "C", "D")
        val result = useCase(full, "E", 4)
        assertEquals(4, result.size)
        assertFalse(result.contains("E"))
    }

    @Test
    fun invoke_belowMaxCapacity_newIdAdded() = runTest {
        val result = useCase(listOf("A", "B", "C"), "D", 4)
        assertEquals(4, result.size)
        assertTrue(result.contains("D"))
    }

    @Test
    fun invoke_removeFromFull_reducesSize() = runTest {
        val result = useCase(listOf("A", "B", "C", "D"), "A", 4)
        assertEquals(3, result.size)
        assertFalse(result.contains("A"))
    }

    @Test
    fun invoke_persistsToRepository() = runTest {
        useCase(listOf("EVENTS"), "KAART", 4)
        assertTrue(savedShortcuts.contains("KAART"))
    }

    @Test
    fun invoke_maxOfOne_toggleAddRemove() = runTest {
        val added = useCase(emptyList(), "EVENTS", 1)
        assertEquals(listOf("EVENTS"), added)

        val removed = useCase(added, "EVENTS", 1)
        assertTrue(removed.isEmpty())
    }

    @Test
    fun invoke_maxOfOne_doesNotAddSecond() = runTest {
        val result = useCase(listOf("EVENTS"), "KAART", 1)
        assertEquals(listOf("EVENTS"), result)
    }
}
