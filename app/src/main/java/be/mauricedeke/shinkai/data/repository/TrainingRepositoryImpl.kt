package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.local.room.dao.StrengthResultDao
import be.mauricedeke.shinkai.data.local.room.entity.StrengthResultEntity
import be.mauricedeke.shinkai.data.remote.client.TrainingClient
import be.mauricedeke.shinkai.data.remote.mapper.toDomain
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
    private val trainingClient: TrainingClient,
    private val strengthResultDao: StrengthResultDao,
    private val trainingNoteRepository: TrainingNoteRepositoryImpl
) : TrainingRepository {

    private var trainingsCache: MutableList<Training>? = null

    override suspend fun getTrainings(): List<Training>? {
        if (trainingsCache == null) {
            val fetched = trainingClient.getTrainings().getOrNull()
                ?.map { it.toDomain() }?.toMutableList() ?: return null
            trainingsCache = fetched
            fetched.forEach { trainingNoteRepository.seedNote(it.id, it.note) }
        }
        return trainingsCache
    }

    override suspend fun getNextTraining(): Training? =
        trainingClient.getNextTraining().getOrNull()?.toDomain()

    override suspend fun getTrainingsByDate(date: LocalDate): List<Training>? =
        getTrainings()?.filter { it.date == date }

    override suspend fun addTraining(training: Training) {
        val dto = trainingClient.createTraining(
            type = training.type,
            startTime = training.startTime,
            endTime = training.endTime,
            date = training.date.toString(),
            injuries = training.injuries,
            sensei = training.sensei
        ).getOrNull()
        val saved = dto?.toDomain() ?: training
        trainingsCache = (trainingsCache ?: mutableListOf()).apply { add(saved) }
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
