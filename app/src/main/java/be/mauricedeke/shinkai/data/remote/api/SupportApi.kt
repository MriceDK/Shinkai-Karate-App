package be.mauricedeke.shinkai.data.remote.api

import be.mauricedeke.shinkai.data.remote.dto.CreateSupportTicketRequestDto
import be.mauricedeke.shinkai.data.remote.dto.SupportTicketDto
import retrofit2.http.Body
import retrofit2.http.POST

interface SupportApi {

    @POST("support")
    suspend fun createTicket(@Body body: CreateSupportTicketRequestDto): SupportTicketDto
}
