package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.Training
import be.mauricedeke.shinkai.domain.repository.TrainingRepository
import java.time.LocalDate
import javax.inject.Inject

class GetTrainingsByDateUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {
    suspend operator fun invoke(date: LocalDate): List<Training>? =
        trainingRepository.getTrainingsByDate(date)
}
