package be.mauricedeke.shinkai.data.remote.api

import be.mauricedeke.shinkai.data.remote.dto.CreateTrainingRequestDto
import be.mauricedeke.shinkai.data.remote.dto.NoteRequestDto
import be.mauricedeke.shinkai.data.remote.dto.TrainingDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TrainingApi {

    @GET("trainings")
    suspend fun getTrainings(): List<TrainingDto>

    @GET("trainings/next")
    suspend fun getNextTraining(): TrainingDto

    @GET("trainings/{id}")
    suspend fun getTrainingById(@Path("id") id: String): TrainingDto

    @POST("trainings")
    suspend fun createTraining(@Body body: CreateTrainingRequestDto): TrainingDto

    @PATCH("trainings/{id}/note")
    suspend fun updateNote(@Path("id") id: String, @Body body: NoteRequestDto)
}
