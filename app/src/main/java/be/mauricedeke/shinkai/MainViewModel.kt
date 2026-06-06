package be.mauricedeke.shinkai

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.data.local.datastore.AppDataStore
import be.mauricedeke.shinkai.data.remote.AuthTokenStore
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val notificationEventBus: NotificationEventBus,
    private val appDataStore: AppDataStore,
    private val tokenStore: AuthTokenStore
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
            val token = appDataStore.accessToken.first()
            if (token != null && isJwtValid(token)) {
                tokenStore.accessToken = token
                _isAuthenticated.value = true
            } else {
                if (token != null) appDataStore.clearAccessToken()
                tokenStore.accessToken = null
                _isAuthenticated.value = false
            }
        }
        viewModelScope.launch {
            notificationEventBus.events.collect { notification ->
                _inAppNotification.emit(notification)
            }
        }
    }

    fun requestPermission(permission: AppPermission) {
        _permissionRequest.value = permission
    }

    fun onPermissionResult() {
        _permissionRequest.value = null
    }

    private fun isJwtValid(token: String): Boolean = try {
        val payload = token.split(".")[1]
        val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        val exp = JSONObject(String(decoded)).getLong("exp")
        System.currentTimeMillis() / 1000 < exp
    } catch (e: Exception) {
        false
    }
}
