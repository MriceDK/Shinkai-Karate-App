package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.data.worker.ReminderScheduler
import javax.inject.Inject

class ScheduleRemindersUseCase @Inject constructor(
    private val reminderScheduler: ReminderScheduler
) {
    suspend operator fun invoke() = reminderScheduler.scheduleAll()
}
