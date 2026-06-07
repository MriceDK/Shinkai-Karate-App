package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.data.remote.client.TrainingSessionClient
import be.mauricedeke.shinkai.data.remote.mapper.toDomain
import be.mauricedeke.shinkai.domain.model.TrainingSession
import javax.inject.Inject

class GetTrainingSessionByIdUseCase @Inject constructor(
    private val trainingSessionClient: TrainingSessionClient
) {
    suspend operator fun invoke(id: String): TrainingSession? =
        trainingSessionClient.getTrainingSessionById(id).getOrNull()?.toDomain()
}
