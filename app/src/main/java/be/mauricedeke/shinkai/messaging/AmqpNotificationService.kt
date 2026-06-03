package be.mauricedeke.shinkai.messaging

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import be.mauricedeke.shinkai.R
import be.mauricedeke.shinkai.data.messaging.MessageConsumer
import be.mauricedeke.shinkai.data.worker.KEY_BODY
import be.mauricedeke.shinkai.data.worker.KEY_TITLE
import be.mauricedeke.shinkai.data.worker.NotificationWorker
import be.mauricedeke.shinkai.data.worker.SERVICE_CHANNEL_ID
import be.mauricedeke.shinkai.data.worker.SERVICE_CHANNEL_NAME
import be.mauricedeke.shinkai.data.worker.SERVICE_NOTIFICATION_ID
import be.mauricedeke.shinkai.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AmqpNotificationService : Service() {

    @Inject lateinit var messageConsumer: MessageConsumer
    @Inject lateinit var getUserProfile: GetUserProfileUseCase

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()
        startForeground(SERVICE_NOTIFICATION_ID, buildForegroundNotification())
        scope.launch {
            val userId = getUserProfile()?.userId?.toString() ?: return@launch
            messageConsumer.onMessageReceived = { raw -> handleMessage(raw) }
            messageConsumer.startConsuming(userId)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onDestroy() {
        messageConsumer.stopConsuming()
        job.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun handleMessage(raw: String) {
        val parts = raw.split("|", limit = 3)
        if (parts.size < 3) return
        val (type, title, body) = parts
        if (type != "reminder") return

        WorkManager.getInstance(applicationContext).enqueue(
            OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(workDataOf(KEY_TITLE to title, KEY_BODY to body))
                .build()
        )
    }

    private fun buildForegroundNotification(): Notification {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(
            NotificationChannel(SERVICE_CHANNEL_ID, SERVICE_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
        )
        return NotificationCompat.Builder(this, SERVICE_CHANNEL_ID)
            .setContentTitle("ShinKai")
            .setContentText("Listening for reminders")
            .setSmallIcon(R.drawable.shinkai_logo)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }
}
