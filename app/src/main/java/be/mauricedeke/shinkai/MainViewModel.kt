package be.mauricedeke.shinkai

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import be.mauricedeke.shinkai.data.messaging.MessageConsumer
import be.mauricedeke.shinkai.data.worker.KEY_BODY
import be.mauricedeke.shinkai.data.worker.KEY_TITLE
import be.mauricedeke.shinkai.data.worker.NotificationWorker
import be.mauricedeke.shinkai.domain.model.NotificationSettings
import be.mauricedeke.shinkai.domain.usecase.GetNotificationSettingsUseCase
import be.mauricedeke.shinkai.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getUserProfile: GetUserProfileUseCase,
    private val getNotificationSettings: GetNotificationSettingsUseCase,
    private val messageConsumer: MessageConsumer
) : ViewModel() {

    init {
        viewModelScope.launch {
            val userId = getUserProfile()?.userId?.toString() ?: return@launch
            messageConsumer.onMessageReceived = { raw ->
                viewModelScope.launch {
                    handleMessage(raw, getNotificationSettings())
                }
            }
            messageConsumer.startConsuming(userId)
        }
    }

    override fun onCleared() {
        messageConsumer.stopConsuming()
    }

    private fun handleMessage(raw: String, settings: NotificationSettings) {
        val parts = raw.split("|", limit = 3)
        if (parts.size < 3) return
        val (type, title, body) = parts
        if (!isEnabled(type, settings)) return

        WorkManager.getInstance(context).enqueue(
            OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(workDataOf(KEY_TITLE to title, KEY_BODY to body))
                .build()
        )
    }

    private fun isEnabled(type: String, settings: NotificationSettings): Boolean = when (type) {
        "event" -> settings.eventNotifications
        "training" -> settings.trainingNotifications
        "change" -> settings.changeNotifications
        "exam" -> settings.examNotifications
        "update" -> settings.updateNotifications
        else -> false
    }
}
