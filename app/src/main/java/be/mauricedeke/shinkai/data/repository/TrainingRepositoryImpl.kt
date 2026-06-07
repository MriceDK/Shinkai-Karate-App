package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.local.room.dao.StrengthHistoryDao
import be.mauricedeke.shinkai.data.local.room.dao.StrengthResultDao
import be.mauricedeke.shinkai.data.local.room.entity.StrengthHistoryEntity
import be.mauricedeke.shinkai.data.local.room.entity.StrengthResultEntity
import be.mauricedeke.shinkai.data.remote.client.StrengthClient
import be.mauricedeke.shinkai.data.remote.client.TrainingClient
import be.mauricedeke.shinkai.data.remote.mapper.toBeltColor
import be.mauricedeke.shinkai.data.remote.mapper.toDomain
import be.mauricedeke.shinkai.domain.model.StrengthHistory
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
private const val PUNCH_UNIT = "N"
private const val KIAI_UNIT = "dB"

@Singleton
class TrainingRepositoryImpl @Inject constructor(
    private val trainingClient: TrainingClient,
    private val strengthClient: StrengthClient,
    private val strengthResultDao: StrengthResultDao,
    private val strengthHistoryDao: StrengthHistoryDao,
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

    override suspend fun refreshTrainings(): List<Training>? {
        trainingsCache = null
        return getTrainings()
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

    override suspend fun fetchStrengthBests() {
        val bests = strengthClient.getStrengthBests().getOrNull() ?: return
        bests.forEach { dto ->
            val localType = when (dto.type.uppercase()) {
                "PUNCH", "PUNCHING", "PUNCHING_STRENGTH" -> PUNCH_TYPE
                "KIAI", "KIAI_STRENGTH" -> KIAI_TYPE
                else -> dto.type
            }
            strengthResultDao.upsert(StrengthResultEntity(localType, dto.bestScore.toInt()))
        }
    }

    override suspend fun submitPunchTest(score: Int): StrengthResult? {
        val dto = strengthClient.submitPunchTest(score.toDouble()).getOrNull()
        val beltColor = dto?.beltColor?.toBeltColor() ?: beltColorFromPunch(score)
        val newBest = maxOf(
            strengthResultDao.getBestScore(PUNCH_TYPE) ?: 0,
            score
        )
        strengthResultDao.upsert(StrengthResultEntity(PUNCH_TYPE, newBest))
        strengthHistoryDao.insert(
            StrengthHistoryEntity(
                type = PUNCH_TYPE,
                score = score,
                unit = PUNCH_UNIT,
                beltColor = beltColor?.name,
                timestamp = System.currentTimeMillis()
            )
        )
        return StrengthResult(PUNCH_TYPE, score, beltColor, PUNCH_UNIT)
    }

    override suspend fun submitKiaiTest(score: Int): StrengthResult? {
        val dto = strengthClient.submitKiaiTest(score.toDouble()).getOrNull()
        val beltColor = dto?.beltColor?.toBeltColor() ?: beltColorFromDb(score)
        val newBest = maxOf(
            strengthResultDao.getBestScore(KIAI_TYPE) ?: 0,
            score
        )
        strengthResultDao.upsert(StrengthResultEntity(KIAI_TYPE, newBest))
        strengthHistoryDao.insert(
            StrengthHistoryEntity(
                type = KIAI_TYPE,
                score = score,
                unit = KIAI_UNIT,
                beltColor = beltColor?.name,
                timestamp = System.currentTimeMillis()
            )
        )
        return StrengthResult(KIAI_TYPE, score, beltColor, KIAI_UNIT)
    }

    override fun observeStrengthHistory(): Flow<List<StrengthHistory>> =
        strengthHistoryDao.observeRecent().map { entities ->
            entities.map { e ->
                StrengthHistory(
                    id = e.id,
                    type = e.type,
                    score = e.score,
                    unit = e.unit,
                    beltColor = e.beltColor?.toBeltColor(),
                    timestamp = e.timestamp
                )
            }
        }
}
