package be.mauricedeke.shinkai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.data.remote.SessionEventBus
import be.mauricedeke.shinkai.domain.usecase.ClearSessionUseCase
import be.mauricedeke.shinkai.domain.usecase.RestoreSessionUseCase
import be.mauricedeke.shinkai.messaging.InAppNotification
import be.mauricedeke.shinkai.messaging.NotificationEventBus
import be.mauricedeke.shinkai.ui.permissions.AppPermission
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val notificationEventBus: NotificationEventBus,
    private val restoreSession: RestoreSessionUseCase,
    private val clearSession: ClearSessionUseCase,
    private val sessionEventBus: SessionEventBus
) : ViewModel() {

    private val _inAppNotification = MutableSharedFlow<InAppNotification>()
    val inAppNotification: SharedFlow<InAppNotification> = _inAppNotification.asSharedFlow()

    private val _permissionRequest = MutableStateFlow<AppPermission?>(null)
    val permissionRequest: StateFlow<AppPermission?> = _permissionRequest.asStateFlow()

    // null = still loading, true = valid token, false = no/expired token
    private val _isAuthenticated = MutableStateFlow<Boolean?>(null)
    val isAuthenticated: StateFlow<Boolean?> = _isAuthenticated.asStateFlow()

    init {
        viewModelScope.launch {
            _isAuthenticated.value = restoreSession()
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

    fun requestPermission(permission: AppPermission) {
        _permissionRequest.value = permission
    }

    fun onPermissionResult() {
        _permissionRequest.value = null
    }
}
