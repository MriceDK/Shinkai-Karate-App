package be.mauricedeke.shinkai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.messaging.InAppNotification
import be.mauricedeke.shinkai.messaging.NotificationEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val notificationEventBus: NotificationEventBus
) : ViewModel() {

    private val _inAppNotification = MutableSharedFlow<InAppNotification>()
    val inAppNotification: SharedFlow<InAppNotification> = _inAppNotification.asSharedFlow()

    init {
        viewModelScope.launch {
            notificationEventBus.events.collect { notification ->
                _inAppNotification.emit(notification)
            }
        }
    }
}
