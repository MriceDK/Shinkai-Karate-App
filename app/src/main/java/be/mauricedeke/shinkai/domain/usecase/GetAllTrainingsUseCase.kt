package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.Training
import be.mauricedeke.shinkai.domain.repository.TrainingRepository
import javax.inject.Inject

class GetAllTrainingsUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {
    suspend operator fun invoke(): List<Training>? = trainingRepository.getTrainings()
}
