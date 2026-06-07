package be.mauricedeke.shinkai.geofence

data class PendingLogPrompt(
    val name: String,
    val date: String,
    val logType: String = "",
    val startTime: String = "",
    val endTime: String = ""
)
