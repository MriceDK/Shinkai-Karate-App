package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.Training
import be.mauricedeke.shinkai.domain.repository.TrainingRepository
import javax.inject.Inject

class GetNextTrainingUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {
    suspend operator fun invoke(): Training? =
        trainingRepository.getNextTraining()
}
