package be.mauricedeke.shinkai.domain.repository

import be.mauricedeke.shinkai.domain.model.StrengthResult
import be.mauricedeke.shinkai.domain.model.Training
import java.time.LocalDate

interface TrainingRepository {
    suspend fun getTrainings(): List<Training>?
    suspend fun getTrainingsByDate(date: LocalDate): List<Training>?
    suspend fun getStrengthResults(): List<StrengthResult>?
}
