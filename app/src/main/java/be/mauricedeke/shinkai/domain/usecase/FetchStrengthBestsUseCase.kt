package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.repository.TrainingRepository
import javax.inject.Inject

class FetchStrengthBestsUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {
    suspend operator fun invoke() = trainingRepository.fetchStrengthBests()
}
