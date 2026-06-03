package be.howest.annaudenaert.sweetdroiddelights.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import be.howest.annaudenaert.sweetdroiddelights.domain.model.OrderStatusMessage
import com.squareup.moshi.Moshi
import javax.inject.Inject

class NotificationWorker (
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val orderId = inputData.getString("ORDER_ID") ?: return Result.failure()
        NotificationHelper.showOrderConfirmationNotification(applicationContext, orderId)
        return Result.success()
    }
}