package be.mauricedeke.shinkai.data.remote.client

import be.mauricedeke.shinkai.data.remote.api.TrainingSessionApi
import be.mauricedeke.shinkai.data.remote.dto.TrainingSessionDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainingSessionClient @Inject constructor(private val api: TrainingSessionApi) {

    suspend fun getTrainingSessions(): Result<List<TrainingSessionDto>> =
        runCatching { api.getTrainingSessions() }

    suspend fun getUpcomingTrainingSession(): Result<TrainingSessionDto> =
        runCatching { api.getUpcomingTrainingSession() }
}
