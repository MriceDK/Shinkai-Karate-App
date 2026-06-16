package be.mauricedeke.shinkai.ui.permissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.data.messaging.AmqpServiceController
import be.mauricedeke.shinkai.data.remote.SessionEventBus
import be.mauricedeke.shinkai.domain.usecase.ClearSessionUseCase
import be.mauricedeke.shinkai.domain.usecase.RestoreSessionUseCase
import be.mauricedeke.shinkai.domain.usecase.ScheduleRemindersUseCase
import be.mauricedeke.shinkai.domain.usecase.SetupGeofencesUseCase
import be.mauricedeke.shinkai.data.geofence.PendingLogPrompt
import be.mauricedeke.shinkai.data.geofence.PendingLogStore
import be.mauricedeke.shinkai.data.messaging.InAppNotification
import be.mauricedeke.shinkai.data.messaging.NotificationEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val notificationEventBus: NotificationEventBus,
    private val restoreSession: RestoreSessionUseCase,
    private val clearSession: ClearSessionUseCase,
    private val sessionEventBus: SessionEventBus,
    private val scheduleReminders: ScheduleRemindersUseCase,
    private val setupGeofences: SetupGeofencesUseCase,
    private val pendingLogStore: PendingLogStore,
    private val amqpServiceController: AmqpServiceController
) : ViewModel() {

    private val _inAppNotification = MutableSharedFlow<InAppNotification>()
    val inAppNotification: SharedFlow<InAppNotification> = _inAppNotification.asSharedFlow()

    private val _permissionRequest = MutableStateFlow<AppPermission?>(null)
    val permissionRequest: StateFlow<AppPermission?> = _permissionRequest.asStateFlow()

    private val _pendingLogPrompt = MutableStateFlow<PendingLogPrompt?>(pendingLogStore.get())
    val pendingLogPrompt: StateFlow<PendingLogPrompt?> = _pendingLogPrompt.asStateFlow()

    private val _resumeTick = MutableStateFlow(0)
    val resumeTick: StateFlow<Int> = _resumeTick.asStateFlow()

    // null = still loading, true = valid token, false = no/expired token
    private val _isAuthenticated = MutableStateFlow<Boolean?>(null)
    val isAuthenticated: StateFlow<Boolean?> = _isAuthenticated.asStateFlow()

    init {
        viewModelScope.launch {
            val authenticated = restoreSession()
            _isAuthenticated.value = authenticated
            if (authenticated == true) {
                scheduleReminders()
                setupGeofences()
                amqpServiceController.start()
            }
        }
        viewModelScope.launch {
            notificationEventBus.events.collect { notification ->
                _inAppNotification.emit(notification)
            }
        }
        viewModelScope.launch {
            sessionEventBus.sessionExpired.collect {
                clearSession()
                _isAuthenticated.value = false
            }
        }
    }

    fun onLoginSuccess() {
        viewModelScope.launch {
            scheduleReminders()
            setupGeofences()
        }
        amqpServiceController.start()
    }

    fun requestPermission(permission: AppPermission) {
        _permissionRequest.value = permission
    }

    fun onPermissionResult() {
        _permissionRequest.value = null
    }

    fun onAppResumed() {
        _resumeTick.update { it + 1 }
    }

    fun recheckPendingLog() {
        val prompt = pendingLogStore.get()
        if (prompt != null) _pendingLogPrompt.value = prompt
    }

    fun dismissLogPrompt() {
        pendingLogStore.clear()
        _pendingLogPrompt.value = null
    }

    fun confirmLogPrompt() {
        pendingLogStore.clear()
        _pendingLogPrompt.value = null
    }
}
