package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.local.datastore.AppDataStore
import be.mauricedeke.shinkai.data.local.room.dao.KataDao
import be.mauricedeke.shinkai.data.local.room.entity.KataEntity
import be.mauricedeke.shinkai.data.remote.client.KataClient
import be.mauricedeke.shinkai.data.remote.client.VersionsClient
import be.mauricedeke.shinkai.domain.model.BeltColor
import be.mauricedeke.shinkai.domain.model.Kata
import be.mauricedeke.shinkai.domain.repository.KataRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KataRepositoryImpl @Inject constructor(
    private val kataClient: KataClient,
    private val versionsClient: VersionsClient,
    private val appDataStore: AppDataStore,
    private val kataDao: KataDao
) : KataRepository {

    override suspend fun getKatas(): List<Kata>? {
        val apiVersions = versionsClient.getVersions().getOrNull()?.katas ?: emptyMap()
        val storedVersions = appDataStore.getKataVersions()
        val cached = kataDao.getAll()

        if (apiVersions.isNotEmpty() && apiVersions == storedVersions && cached.isNotEmpty()) {
            return cached.map { it.toDomain() }
        }

        val dtos = kataClient.getKatas().getOrNull()
            ?: return cached.takeIf { it.isNotEmpty() }?.map { it.toDomain() }

        kataDao.deleteAll()
        kataDao.upsertAll(dtos.map { dto ->
            KataEntity(
                id = dto.id,
                name = dto.name,
                belt = dto.belt ?: "",
                beltColor = dto.beltColor ?: "",
                description = dto.description,
                moves = dto.moves
            )
        })

        if (apiVersions.isNotEmpty()) appDataStore.setKataVersions(apiVersions)

        return dtos.map { dto ->
            Kata(
                id = dto.id,
                name = dto.name,
                belt = dto.belt ?: "",
                beltColor = runCatching { BeltColor.valueOf(dto.beltColor ?: "") }.getOrElse { BeltColor.YELLOW },
                description = dto.description,
                moves = dto.moves
            )
        }
    }

    private fun KataEntity.toDomain() = Kata(
        id = id,
        name = name,
        belt = belt,
        beltColor = runCatching { BeltColor.valueOf(beltColor) }.getOrElse { BeltColor.YELLOW },
        description = description,
        moves = moves
    )
}
