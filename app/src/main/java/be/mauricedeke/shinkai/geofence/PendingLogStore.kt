package be.mauricedeke.shinkai.geofence

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PendingLogStore @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun get(): PendingLogPrompt? {
        val name = prefs.getString(KEY_NAME, null) ?: return null
        val date = prefs.getString(KEY_DATE, null) ?: return null
        return PendingLogPrompt(
            name = name,
            date = date,
            logType = prefs.getString(KEY_LOG_TYPE, "") ?: "",
            startTime = prefs.getString(KEY_START_TIME, "") ?: "",
            endTime = prefs.getString(KEY_END_TIME, "") ?: ""
        )
    }

    fun clear() {
        prefs.edit()
            .remove(KEY_NAME)
            .remove(KEY_DATE)
            .remove(KEY_LOG_TYPE)
            .remove(KEY_START_TIME)
            .remove(KEY_END_TIME)
            .apply()
    }

    companion object {
        const val PREFS_NAME = "shinkai_geofence"
        const val KEY_NAME = "pending_name"
        const val KEY_DATE = "pending_date"
        const val KEY_LOG_TYPE = "pending_log_type"
        const val KEY_START_TIME = "pending_start_time"
        const val KEY_END_TIME = "pending_end_time"
    }
}
