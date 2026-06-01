package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.Belt
import be.mauricedeke.shinkai.domain.repository.TechniekRepository
import javax.inject.Inject

class GetBeltsUseCase @Inject constructor(
    private val techniekRepository: TechniekRepository
) {
    suspend operator fun invoke(): List<Belt> = techniekRepository.getBelts()
}
