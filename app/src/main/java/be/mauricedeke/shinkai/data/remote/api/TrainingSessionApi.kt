package be.mauricedeke.shinkai.data.remote.api

import be.mauricedeke.shinkai.data.remote.dto.TrainingSessionDto
import retrofit2.http.GET
import retrofit2.http.Path

interface TrainingSessionApi {

    @GET("training-sessions")
    suspend fun getTrainingSessions(): List<TrainingSessionDto>

    @GET("training-sessions/upcoming")
    suspend fun getUpcomingTrainingSession(): List<TrainingSessionDto>

    @GET("training-sessions/{id}")
    suspend fun getTrainingSessionById(@Path("id") id: String): TrainingSessionDto
}
