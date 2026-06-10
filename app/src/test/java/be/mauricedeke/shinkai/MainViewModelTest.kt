package be.mauricedeke.shinkai

import be.mauricedeke.shinkai.data.remote.SessionEventBus
import be.mauricedeke.shinkai.domain.usecase.ClearSessionUseCase
import be.mauricedeke.shinkai.domain.usecase.RestoreSessionUseCase
import be.mauricedeke.shinkai.domain.usecase.ScheduleRemindersUseCase
import be.mauricedeke.shinkai.domain.usecase.SetupGeofencesUseCase
import be.mauricedeke.shinkai.geofence.PendingLogPrompt
import be.mauricedeke.shinkai.geofence.PendingLogStore
import be.mauricedeke.shinkai.messaging.NotificationEventBus
import be.mauricedeke.shinkai.ui.permissions.AppPermission
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private val restoreSession: RestoreSessionUseCase = mockk(relaxed = true)
    private val clearSession: ClearSessionUseCase = mockk(relaxed = true)
    private val scheduleReminders: ScheduleRemindersUseCase = mockk(relaxed = true)
    private val setupGeofences: SetupGeofencesUseCase = mockk(relaxed = true)
    private val pendingLogStore: PendingLogStore = mockk(relaxed = true)
    private lateinit var sessionEventBus: SessionEventBus
    private lateinit var vm: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { restoreSession() } returns true
        every { pendingLogStore.get() } returns null
        sessionEventBus = SessionEventBus()
        vm = buildVm()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildVm() = MainViewModel(
        notificationEventBus = NotificationEventBus(),
        restoreSession = restoreSession,
        clearSession = clearSession,
        sessionEventBus = sessionEventBus,
        scheduleReminders = scheduleReminders,
        setupGeofences = setupGeofences,
        pendingLogStore = pendingLogStore,
        context =
    )

    @Test
    fun init_authenticatedSession_isAuthenticatedTrue() {
        assertEquals(true, vm.isAuthenticated.value)
    }

    @Test
    fun init_noSession_isAuthenticatedFalse() {
        coEvery { restoreSession() } returns false
        assertEquals(false, buildVm().isAuthenticated.value)
    }

    @Test
    fun requestPermission_setsPermissionRequest() {
        vm.requestPermission(AppPermission.Camera)
        assertEquals(AppPermission.Camera, vm.permissionRequest.value)
    }

    @Test
    fun requestPermission_differentPermissions_setCorrectly() {
        vm.requestPermission(AppPermission.Notifications)
        assertEquals(AppPermission.Notifications, vm.permissionRequest.value)

        vm.requestPermission(AppPermission.Location)
        assertEquals(AppPermission.Location, vm.permissionRequest.value)
    }

    @Test
    fun onPermissionResult_clearsPermissionRequest() {
        vm.requestPermission(AppPermission.RecordAudio)
        vm.onPermissionResult()
        assertNull(vm.permissionRequest.value)
    }

    @Test
    fun dismissLogPrompt_clearsPendingState() {
        vm.dismissLogPrompt()
        assertNull(vm.pendingLogPrompt.value)
    }

    @Test
    fun dismissLogPrompt_callsClearOnStore() {
        vm.dismissLogPrompt()
        verify { pendingLogStore.clear() }
    }

    @Test
    fun confirmLogPrompt_clearsPendingState() {
        vm.confirmLogPrompt()
        assertNull(vm.pendingLogPrompt.value)
    }

    @Test
    fun confirmLogPrompt_callsClearOnStore() {
        vm.confirmLogPrompt()
        verify { pendingLogStore.clear() }
    }

    @Test
    fun recheckPendingLog_storeHasPrompt_updatesState() {
        val prompt = PendingLogPrompt(name = "Dojo Gent", date = "2026-06-10")
        every { pendingLogStore.get() } returns prompt
        vm.recheckPendingLog()
        assertEquals(prompt, vm.pendingLogPrompt.value)
    }

    @Test
    fun recheckPendingLog_storeEmpty_stateRemainsNull() {
        every { pendingLogStore.get() } returns null
        vm.recheckPendingLog()
        assertNull(vm.pendingLogPrompt.value)
    }

    @Test
    fun init_withPendingLog_stateIsSet() {
        val prompt = PendingLogPrompt(name = "Training", date = "2026-06-08")
        every { pendingLogStore.get() } returns prompt
        val vmWithLog = buildVm()
        assertEquals(prompt, vmWithLog.pendingLogPrompt.value)
    }

    @Test
    fun sessionExpired_setsIsAuthenticatedFalse() {
        assertTrue(vm.isAuthenticated.value == true)
        sessionEventBus.notifySessionExpired()
        assertFalse(vm.isAuthenticated.value == true)
    }
}
