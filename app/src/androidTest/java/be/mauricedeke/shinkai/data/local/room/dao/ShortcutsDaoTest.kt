package be.mauricedeke.shinkai.data.local.room.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import be.mauricedeke.shinkai.data.local.ShinkaiDatabase
import be.mauricedeke.shinkai.data.local.room.entity.ShortcutsEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShortcutsDaoTest {

    private lateinit var db: ShinkaiDatabase
    private lateinit var dao: ShortcutsDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, ShinkaiDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.shortcutsDao()
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
        dao.upsert(ShortcutsEntity(shortcuts = "EVENTS,KAART"))
        val result = dao.get()
        assertNotNull(result)
        assertEquals("EVENTS,KAART", result?.shortcuts)
    }

    @Test
    fun upsert_twice_secondUpsertWins() = runTest {
        dao.upsert(ShortcutsEntity(shortcuts = "EVENTS"))
        dao.upsert(ShortcutsEntity(shortcuts = "KAART,LEXICON"))
        assertEquals("KAART,LEXICON", dao.get()?.shortcuts)
    }

    @Test
    fun upsert_defaultShortcuts_areCorrect() = runTest {
        dao.upsert(ShortcutsEntity())
        val result = dao.get()
        assertEquals("EVENTS,KAART,LEXICON,TECHNIEKEN", result?.shortcuts)
    }

    @Test
    fun upsert_singleShortcut_persistedCorrectly() = runTest {
        dao.upsert(ShortcutsEntity(shortcuts = "STRENGTH_TEST"))
        assertEquals("STRENGTH_TEST", dao.get()?.shortcuts)
    }

    @Test
    fun upsert_emptyShortcuts_persistedCorrectly() = runTest {
        dao.upsert(ShortcutsEntity(shortcuts = ""))
        assertEquals("", dao.get()?.shortcuts)
    }
}
