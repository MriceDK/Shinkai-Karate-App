package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.data.remote.client.TrainingSessionClient
import be.mauricedeke.shinkai.data.remote.mapper.toDomain
import be.mauricedeke.shinkai.domain.model.Training
import javax.inject.Inject

class GetNextTrainingSessionUseCase @Inject constructor(
    private val trainingSessionClient: TrainingSessionClient
) {
    suspend operator fun invoke(): Training? =
        trainingSessionClient.getUpcomingTrainingSession().getOrNull()?.toDomain()
}
