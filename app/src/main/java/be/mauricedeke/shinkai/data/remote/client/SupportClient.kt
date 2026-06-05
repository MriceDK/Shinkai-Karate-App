package be.mauricedeke.shinkai.data.remote.client

import be.mauricedeke.shinkai.data.remote.api.SupportApi
import be.mauricedeke.shinkai.data.remote.dto.CreateSupportTicketRequestDto
import be.mauricedeke.shinkai.data.remote.dto.SupportTicketDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupportClient @Inject constructor(private val api: SupportApi) {

    suspend fun createTicket(subject: String, message: String): Result<SupportTicketDto> =
        runCatching { api.createTicket(CreateSupportTicketRequestDto(subject, message)) }
}
