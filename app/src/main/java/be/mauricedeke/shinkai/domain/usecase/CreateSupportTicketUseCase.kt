package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.data.remote.client.SupportClient
import javax.inject.Inject

class CreateSupportTicketUseCase @Inject constructor(
    private val supportClient: SupportClient
) {
    suspend operator fun invoke(subject: String, message: String): Result<Unit> =
        supportClient.createTicket(subject, message).map { }
}
