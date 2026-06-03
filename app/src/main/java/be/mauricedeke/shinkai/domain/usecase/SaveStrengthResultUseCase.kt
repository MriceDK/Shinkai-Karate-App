package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.repository.TrainingRepository
import javax.inject.Inject

class SaveStrengthResultUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {
    suspend operator fun invoke(type: String, bestScore: Int) =
        trainingRepository.saveStrengthResult(type, bestScore)
}
