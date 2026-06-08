package be.mauricedeke.shinkai.data.local.room.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import be.mauricedeke.shinkai.data.local.ShinkaiDatabase
import be.mauricedeke.shinkai.data.local.room.entity.UserProfileEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class UserProfileDaoTest {

    private lateinit var db: ShinkaiDatabase
    private lateinit var dao: UserProfileDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, ShinkaiDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.userProfileDao()
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
    fun upsert_thenGet_returnsProfile() = runTest {
        val userId = UUID.randomUUID()
        dao.upsert(UserProfileEntity(userId = userId, name = "Jan", email = "jan@shinkai.be", belt = "Blue belt"))
        val result = dao.get()
        assertNotNull(result)
        assertEquals("Jan", result?.name)
        assertEquals("jan@shinkai.be", result?.email)
        assertEquals("Blue belt", result?.belt)
        assertEquals(userId, result?.userId)
    }

    @Test
    fun upsert_twice_updatesProfile() = runTest {
        dao.upsert(UserProfileEntity(name = "Old Name"))
        dao.upsert(UserProfileEntity(name = "New Name"))
        assertEquals("New Name", dao.get()?.name)
    }

    @Test
    fun upsert_defaultBelt_isYellowBelt() = runTest {
        dao.upsert(UserProfileEntity())
        assertEquals("Yellow belt", dao.get()?.belt)
    }

    @Test
    fun upsert_nullProfilePicture_persistedCorrectly() = runTest {
        dao.upsert(UserProfileEntity(profilePictureUri = null))
        assertNull(dao.get()?.profilePictureUri)
    }

    @Test
    fun upsert_withProfilePicture_persistedCorrectly() = runTest {
        dao.upsert(UserProfileEntity(profilePictureUri = "content://media/image/1"))
        assertEquals("content://media/image/1", dao.get()?.profilePictureUri)
    }

    @Test
    fun observe_emptyDatabase_emitsNull() = runTest {
        val result = dao.observe().first()
        assertNull(result)
    }

    @Test
    fun observe_afterUpsert_emitsProfile() = runTest {
        dao.upsert(UserProfileEntity(name = "Marie"))
        val result = dao.observe().first()
        assertNotNull(result)
        assertEquals("Marie", result?.name)
    }
}
