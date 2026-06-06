package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.local.datastore.AppDataStore
import be.mauricedeke.shinkai.data.local.room.dao.BeltDao
import be.mauricedeke.shinkai.data.local.room.dao.TechniekDao
import be.mauricedeke.shinkai.data.local.room.entity.BeltEntity
import be.mauricedeke.shinkai.data.local.room.entity.TechniekEntity
import be.mauricedeke.shinkai.data.remote.client.BeltClient
import be.mauricedeke.shinkai.data.remote.client.VersionsClient
import be.mauricedeke.shinkai.data.remote.dto.BeltDto
import be.mauricedeke.shinkai.data.remote.dto.ProgrammeDto
import be.mauricedeke.shinkai.data.remote.mapper.toDomain
import be.mauricedeke.shinkai.domain.model.Belt
import be.mauricedeke.shinkai.domain.model.BeltColor
import be.mauricedeke.shinkai.domain.model.BeltProgram
import be.mauricedeke.shinkai.domain.model.ProgramSection
import be.mauricedeke.shinkai.domain.model.Techniek
import be.mauricedeke.shinkai.domain.repository.TechniekRepository
import com.squareup.moshi.Moshi
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TechniekRepositoryImpl @Inject constructor(
    private val beltClient: BeltClient,
    private val versionsClient: VersionsClient,
    private val appDataStore: AppDataStore,
    private val beltDao: BeltDao,
    private val techniekDao: TechniekDao,
    private val moshi: Moshi
) : TechniekRepository {

    private var beltsCache: List<Belt>? = null

    private val programmeAdapter by lazy { moshi.adapter(ProgrammeDto::class.java) }

    override suspend fun getBelts(): List<Belt>? {
        beltsCache?.let { return it }

        val apiVersions = versionsClient.getVersions().getOrNull()?.belts ?: emptyMap()
        val storedVersions = appDataStore.getBeltVersions()
        val roomBelts = beltDao.getAll()

        if (apiVersions.isNotEmpty() && apiVersions == storedVersions && roomBelts.isNotEmpty()) {
            val technieken = techniekDao.getAll()
            val belts = roomBelts.map { it.toDomain(technieken.filter { t -> t.beltName == it.name }) }
            beltsCache = belts
            return belts
        }

        val dtos = beltClient.getBelts().getOrNull() ?: return roomBelts
            .takeIf { it.isNotEmpty() }
            ?.let { entities ->
                val technieken = techniekDao.getAll()
                entities.map { it.toDomain(technieken.filter { t -> t.beltName == it.name }) }
            }

        val belts = dtos.map { it.toDomain() }

        beltDao.deleteAll()
        techniekDao.deleteAll()
        beltDao.upsertAll(dtos.map { it.toEntity() })
        techniekDao.upsertAll(dtos.flatMap { belt ->
            belt.technieken.map { t ->
                TechniekEntity(
                    id = "${belt.name}:${t.name}",
                    name = t.name,
                    beltName = belt.name,
                    description = t.description,
                    programma = t.programme ?: ""
                )
            }
        })

        if (apiVersions.isNotEmpty()) appDataStore.setBeltVersions(apiVersions)

        beltsCache = belts
        return belts
    }

    override suspend fun getBeltByName(name: String): Belt? =
        getBelts()?.find { it.name == name }

    override suspend fun getTechnieksByBelt(belt: String): List<Techniek> =
        getBelts()?.find { it.name == belt }?.technieken ?: emptyList()

    private fun BeltDto.toEntity() = BeltEntity(
        name = name,
        beltColor = beltColor,
        programmeJson = runCatching { programmeAdapter.toJson(programme) }.getOrElse { "{\"sections\":[]}" }
    )

    private fun BeltEntity.toDomain(technieken: List<TechniekEntity>): Belt {
        val programme = runCatching { programmeAdapter.fromJson(programmeJson) }.getOrNull()
        return Belt(
            name = name,
            beltColor = runCatching { BeltColor.valueOf(beltColor) }.getOrElse { BeltColor.YELLOW },
            pogramma = BeltProgram(programme?.sections?.map { ProgramSection(it.title, it.items) } ?: emptyList()),
            technieken = technieken.map { t ->
                Techniek(id = UUID.randomUUID(), name = t.name, belt = t.beltName, description = t.description, programma = t.programma)
            },
            notes = ""
        )
    }
}
