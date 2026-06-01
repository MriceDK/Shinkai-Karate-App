package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.fake.FakeDataSource
import be.mauricedeke.shinkai.domain.model.StrengthResult
import be.mauricedeke.shinkai.domain.model.Training
import be.mauricedeke.shinkai.domain.repository.TrainingRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainingRepositoryImpl @Inject constructor() : TrainingRepository {
    override suspend fun getTrainings(): List<Training>? = FakeDataSource.trainings
    override suspend fun getTrainingsByDate(date: LocalDate): List<Training>? =
        FakeDataSource.trainings?.filter { it.date == date }
    override suspend fun getStrengthResults(): List<StrengthResult>? = FakeDataSource.strengthResults
}
