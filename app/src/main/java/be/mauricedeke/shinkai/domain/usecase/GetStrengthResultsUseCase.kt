package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.StrengthResult
import be.mauricedeke.shinkai.domain.repository.TrainingRepository
import javax.inject.Inject

class GetStrengthResultsUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {
    suspend operator fun invoke(): List<StrengthResult> = trainingRepository.getStrengthResults()
}
