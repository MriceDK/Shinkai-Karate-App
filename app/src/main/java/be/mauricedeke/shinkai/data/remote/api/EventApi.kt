package be.mauricedeke.shinkai.data.remote.api

import be.mauricedeke.shinkai.data.remote.dto.EventDto
import be.mauricedeke.shinkai.data.remote.dto.RsvpRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface EventApi {

    @GET("events")
    suspend fun getEvents(): List<EventDto>

    @GET("events/inbox")
    suspend fun getInboxEvents(): List<EventDto>

    @GET("events/{id}")
    suspend fun getEventById(@Path("id") id: String): EventDto

    @PUT("events/{id}/rsvp")
    suspend fun setRsvp(@Path("id") id: String, @Body body: RsvpRequestDto)
}
