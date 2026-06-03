package be.mauricedeke.shinkai.domain.repository

import be.mauricedeke.shinkai.domain.model.StrengthResult
import be.mauricedeke.shinkai.domain.model.Training
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

interface TrainingRepository {
    suspend fun getTrainings(): List<Training>?
    suspend fun getTrainingsByDate(date: LocalDate): List<Training>?
    fun observeStrengthResults(): Flow<List<StrengthResult>>
    suspend fun saveStrengthResult(type: String, bestScore: Int)
}
