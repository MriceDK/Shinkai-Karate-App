package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.model.StrengthResult
import be.mauricedeke.shinkai.domain.repository.TrainingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStrengthResultsUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {
    operator fun invoke(): Flow<List<StrengthResult>> = trainingRepository.observeStrengthResults()
}
