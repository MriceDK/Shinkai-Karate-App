package be.mauricedeke.shinkai.domain.repository

import be.mauricedeke.shinkai.domain.model.StrengthResult
import be.mauricedeke.shinkai.domain.model.Training
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TrainingRepository {
    suspend fun getTrainings(): List<Training>?
    suspend fun getNextTraining(): Training?
    suspend fun getTrainingsByDate(date: LocalDate): List<Training>?
    suspend fun addTraining(training: Training)
    fun observeStrengthResults(): Flow<List<StrengthResult>>
    suspend fun saveStrengthResult(type: String, bestScore: Int)
}
