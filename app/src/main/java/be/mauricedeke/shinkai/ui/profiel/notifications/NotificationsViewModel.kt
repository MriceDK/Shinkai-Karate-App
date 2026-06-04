package be.mauricedeke.shinkai.ui.profiel.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.model.NotificationSettings
import be.mauricedeke.shinkai.domain.usecase.GetNotificationSettingsUseCase
import be.mauricedeke.shinkai.domain.usecase.UpdateNotificationSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotificationSettings: GetNotificationSettingsUseCase,
    private val updateNotificationSettings: UpdateNotificationSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(settings = getNotificationSettings()) }
        }
    }

    fun onSettingsChanged(settings: NotificationSettings) {
        _uiState.update { it.copy(settings = settings) }
        viewModelScope.launch { updateNotificationSettings(settings) }
    }
}
