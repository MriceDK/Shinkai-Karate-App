package be.mauricedeke.shinkai.data.messaging

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmqpServiceControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AmqpServiceController {

    override fun start() {
        context.startForegroundService(Intent(context, AmqpNotificationService::class.java))
    }

    override fun stop() {
        context.stopService(Intent(context, AmqpNotificationService::class.java))
    }
}
