package be.mauricedeke.shinkai.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventDto(
    val id: String,
    val title: String,
    val startTime: String,
    val endTime: String,
    val date: String,
    val localDate: String,
    val location: String,
    val city: String,
    val description: String,
    val rsvp: Boolean?,
    val lat: Double?,
    val lng: Double?
)
