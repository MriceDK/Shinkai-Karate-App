package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.Techniek
import be.mauricedeke.shinkai.domain.repository.TechniekRepository
import javax.inject.Inject

class GetTechnieksByBeltUseCase @Inject constructor(
    private val techniekRepository: TechniekRepository
) {
    suspend operator fun invoke(belt: String): List<Techniek> =
        techniekRepository.getTechnieksByBelt(belt)
}
