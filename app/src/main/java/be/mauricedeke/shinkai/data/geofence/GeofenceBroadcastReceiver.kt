package be.mauricedeke.shinkai.data.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import be.mauricedeke.shinkai.data.worker.NotificationHelper
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val event = GeofencingEvent.fromIntent(intent) ?: return
        if (event.hasError()) return
        if (event.geofenceTransition != Geofence.GEOFENCE_TRANSITION_ENTER) return

        val now = LocalDateTime.now()

        event.triggeringGeofences?.forEach { geofence ->
            // requestId format: name|date|startTime|endTime|logType
            val parts = geofence.requestId.split("|", limit = 5)
            if (parts.size < 4) return@forEach

            val name = parts[0]
            val date = runCatching { LocalDate.parse(parts[1]) }.getOrNull() ?: return@forEach
            val startTime = runCatching { LocalTime.parse(parts[2]) }.getOrNull() ?: return@forEach
            val endTime = runCatching { LocalTime.parse(parts[3]) }.getOrNull() ?: return@forEach
            val logType = if (parts.size >= 5) parts[4] else ""

            val start = LocalDateTime.of(date, startTime)
            val end = LocalDateTime.of(date, endTime)

            if (now.isAfter(start) && now.isBefore(end)) {
                context.getSharedPreferences(PendingLogStore.PREFS_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putString(PendingLogStore.KEY_NAME, name)
                    .putString(PendingLogStore.KEY_DATE, parts[1])
                    .putString(PendingLogStore.KEY_LOG_TYPE, logType)
                    .putString(PendingLogStore.KEY_START_TIME, parts[2])
                    .putString(PendingLogStore.KEY_END_TIME, parts[3])
                    .apply()

                NotificationHelper.show(
                    context,
                    "Training bezig!",
                    "Open de app om je aanwezigheid bij $name te loggen."
                )
            }
        }
    }
}
