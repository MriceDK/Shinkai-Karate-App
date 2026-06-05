package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.fake.FakeDataSource
import be.mauricedeke.shinkai.data.local.room.dao.StrengthResultDao
import be.mauricedeke.shinkai.data.local.room.entity.StrengthResultEntity
import be.mauricedeke.shinkai.domain.model.StrengthResult
import be.mauricedeke.shinkai.domain.model.Training
import be.mauricedeke.shinkai.domain.model.beltColorFromDb
import be.mauricedeke.shinkai.domain.model.beltColorFromPunch
import be.mauricedeke.shinkai.domain.repository.TrainingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

private const val PUNCH_TYPE = "Punching Strength"
private const val KIAI_TYPE = "Kiai Strength"

@Singleton
class TrainingRepositoryImpl @Inject constructor(
    private val strengthResultDao: StrengthResultDao
) : TrainingRepository {
    override suspend fun getTrainings(): List<Training> = FakeDataSource.trainings
    override suspend fun getTrainingsByDate(date: LocalDate): List<Training> =
        FakeDataSource.trainings.filter { it.date == date }
    override suspend fun addTraining(training: Training) {
        FakeDataSource.trainings.add(training)
    }

    override fun observeStrengthResults(): Flow<List<StrengthResult>> =
        strengthResultDao.observeAll().map { entities ->
            val punchScore = entities.firstOrNull { it.type == PUNCH_TYPE }?.bestScore ?: 0
            val kiaiScore = entities.firstOrNull { it.type == KIAI_TYPE }?.bestScore ?: 0
            listOf(
                StrengthResult(PUNCH_TYPE, punchScore, beltColorFromPunch(punchScore).takeIf { punchScore > 0 }, "N"),
                StrengthResult(KIAI_TYPE, kiaiScore, beltColorFromDb(kiaiScore).takeIf { kiaiScore > 0 }, "dB")
            )
        }

    override suspend fun saveStrengthResult(type: String, bestScore: Int) {
        strengthResultDao.upsert(StrengthResultEntity(type, bestScore))
    }
}
