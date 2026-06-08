package be.mauricedeke.shinkai.data.local.room.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import be.mauricedeke.shinkai.data.local.ShinkaiDatabase
import be.mauricedeke.shinkai.data.local.room.entity.NotificationSettingsEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotificationSettingsDaoTest {

    private lateinit var db: ShinkaiDatabase
    private lateinit var dao: NotificationSettingsDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, ShinkaiDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.notificationSettingsDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun get_emptyDatabase_returnsNull() = runTest {
        assertNull(dao.get())
    }

    @Test
    fun upsert_thenGet_returnsInsertedEntity() = runTest {
        val entity = NotificationSettingsEntity(
            eventNotifications = true,
            trainingNotifications = false,
            examNotifications = true,
            changeNotifications = false,
            updateNotifications = true
        )
        dao.upsert(entity)
        val result = dao.get()
        assertNotNull(result)
        assertEquals(false, result?.trainingNotifications)
        assertEquals(false, result?.changeNotifications)
        assertEquals(true, result?.examNotifications)
    }

    @Test
    fun upsert_twice_secondUpsertWins() = runTest {
        dao.upsert(NotificationSettingsEntity(updateNotifications = true))
        dao.upsert(NotificationSettingsEntity(updateNotifications = false))
        assertEquals(false, dao.get()?.updateNotifications)
    }

    @Test
    fun upsert_defaultEntity_defaultValuesAreCorrect() = runTest {
        dao.upsert(NotificationSettingsEntity())
        val result = dao.get()
        assertNotNull(result)
        assertEquals(true, result?.eventNotifications)
        assertEquals(true, result?.trainingNotifications)
        assertEquals(30, result?.reminderMinutesBefore)
        assertEquals(false, result?.examNotifications)
        assertEquals(1440, result?.examReminderMinutesBefore)
    }

    @Test
    fun upsert_reminderMinutes_persistedCorrectly() = runTest {
        dao.upsert(NotificationSettingsEntity(reminderMinutesBefore = 60, trainingReminderMinutesBefore = 15))
        val result = dao.get()
        assertEquals(60, result?.reminderMinutesBefore)
        assertEquals(15, result?.trainingReminderMinutesBefore)
    }
}
