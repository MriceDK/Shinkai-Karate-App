package be.mauricedeke.shinkai.data.remote.client

import be.mauricedeke.shinkai.data.remote.api.TrainingApi
import be.mauricedeke.shinkai.data.remote.dto.CreateTrainingRequestDto
import be.mauricedeke.shinkai.data.remote.dto.NoteRequestDto
import be.mauricedeke.shinkai.data.remote.dto.TrainingDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainingClient @Inject constructor(private val api: TrainingApi) {

    suspend fun getTrainings(): Result<List<TrainingDto>> =
        runCatching { api.getTrainings() }

    suspend fun getNextTraining(): Result<TrainingDto> =
        runCatching { api.getNextTraining() }

    suspend fun getTrainingById(id: String): Result<TrainingDto> =
        runCatching { api.getTrainingById(id) }

    suspend fun createTraining(
        type: String,
        startTime: String,
        endTime: String,
        date: String,
        injuries: String,
        sensei: String
    ): Result<TrainingDto> = runCatching {
        api.createTraining(CreateTrainingRequestDto(type, startTime, endTime, date, injuries, sensei))
    }

    suspend fun updateNote(id: String, note: String): Result<Unit> =
        runCatching { api.updateNote(id, NoteRequestDto(note)) }
}
