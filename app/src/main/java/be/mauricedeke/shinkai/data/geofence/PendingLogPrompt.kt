package be.mauricedeke.shinkai.data.geofence

data class PendingLogPrompt(
    val name: String,
    val date: String,
    val logType: String = "",
    val startTime: String = "",
    val endTime: String = ""
)
