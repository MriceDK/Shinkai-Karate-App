package be.mauricedeke.shinkai.data.worker

import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import be.mauricedeke.shinkai.domain.usecase.GetAllTrainingsUseCase
import be.mauricedeke.shinkai.domain.usecase.GetEventsUseCase
import be.mauricedeke.shinkai.domain.usecase.GetNotificationSettingsUseCase
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ReminderScheduler"

@Singleton
class ReminderScheduler @Inject constructor(
    private val workManager: WorkManager,
    private val getEvents: GetEventsUseCase,
    private val getAllTrainings: GetAllTrainingsUseCase,
    private val getNotificationSettings: GetNotificationSettingsUseCase
) {
    suspend fun scheduleAll() {
        workManager.cancelAllWorkByTag(REMINDER_TAG)
        Log.d(TAG, "Cancelled existing reminders, rescheduling...")
        val settings = getNotificationSettings()

        if (settings.eventNotifications && settings.eventReminderEnabled) {
            val events = getEvents() ?: emptyList()
            val now = LocalDateTime.now()
            Log.d(
                TAG,
                "Scheduling event reminders (${settings.reminderMinutesBefore} min before), found ${events.size} events"
            )
            events.forEach { event ->
                val date = event.localDate ?: run {
                    Log.d(TAG, "Skipping event '${event.title}' — no parseable date")
                    return@forEach
                }
                val time = parseTime(event.startTime) ?: run {
                    Log.d(
                        TAG,
                        "Skipping event '${event.title}' — unparseable startTime '${event.startTime}'"
                    )
                    return@forEach
                }
                val triggerAt = LocalDateTime.of(date, time)
                    .minusMinutes(settings.reminderMinutesBefore.toLong())
                val delay = delayMillis(triggerAt, now)
                if (delay > 0) {
                    Log.d(
                        TAG,
                        "Scheduled reminder for '${event.title}' in ${delay / 60_000} min (fires at $triggerAt)"
                    )
                    workManager.enqueue(
                        OneTimeWorkRequestBuilder<NotificationWorker>()
                            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                            .setInputData(
                                workDataOf(
                                    KEY_TITLE to "Herinnering: ${event.title}",
                                    KEY_BODY to formatBody(settings.reminderMinutesBefore)
                                )
                            )
                            .addTag(REMINDER_TAG)
                            .build()
                    )
                } else {
                    Log.d(
                        TAG,
                        "Skipping event '${event.title}' — trigger time $triggerAt is in the past"
                    )
                }
            }
        }

        if (settings.trainingNotifications && settings.trainingReminderEnabled) {
            val trainings = getAllTrainings() ?: emptyList()
            val now = LocalDateTime.now()
            Log.d(
                TAG,
                "Scheduling training reminders (${settings.trainingReminderMinutesBefore} min before), found ${trainings.size} trainings"
            )
            trainings.forEach { training ->
                val time = parseTime(training.startTime) ?: run {
                    Log.d(
                        TAG,
                        "Skipping training '${training.type}' on ${training.date} — unparseable startTime '${training.startTime}'"
                    )
                    return@forEach
                }
                val triggerAt = LocalDateTime.of(training.date, time)
                    .minusMinutes(settings.trainingReminderMinutesBefore.toLong())
                val delay = delayMillis(triggerAt, now)
                if (delay > 0) {
                    Log.d(
                        TAG,
                        "Scheduled reminder for training '${training.type}' on ${training.date} in ${delay / 60_000} min (fires at $triggerAt)"
                    )
                    workManager.enqueue(
                        OneTimeWorkRequestBuilder<NotificationWorker>()
                            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                            .setInputData(
                                workDataOf(
                                    KEY_TITLE to "Herinnering: Training ${training.type}",
                                    KEY_BODY to formatBody(settings.trainingReminderMinutesBefore)
                                )
                            )
                            .addTag(REMINDER_TAG)
                            .build()
                    )
                } else {
                    Log.d(
                        TAG,
                        "Skipping training '${training.type}' on ${training.date} — trigger time $triggerAt is in the past"
                    )
                }
            }
        }
        Log.d(TAG, "Done scheduling reminders")
    }

    private fun parseTime(raw: String): LocalTime? = runCatching {
        val s = raw.trim()
        if (s.count { it == ':' } >= 2)
            LocalTime.parse(s, DateTimeFormatter.ofPattern("HH:mm:ss"))
        else
            LocalTime.parse(s, DateTimeFormatter.ofPattern("HH:mm"))
    }.getOrNull()

    private fun delayMillis(trigger: LocalDateTime, now: LocalDateTime): Long {
        val triggerEpoch = trigger.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val nowEpoch = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return triggerEpoch - nowEpoch
    }

    private fun formatBody(minutes: Int): String = when {
        minutes < 60 -> "Begint over $minutes minuten."
        minutes == 60 -> "Begint over 1 uur."
        minutes < 1440 -> "Begint over ${minutes / 60} uur."
        minutes == 1440 -> "Begint morgen."
        else -> "Begint over ${minutes / 1440} dagen."
    }
}

const val REMINDER_TAG = "reminder"
