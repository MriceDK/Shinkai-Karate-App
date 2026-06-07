package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.local.datastore.AppDataStore
import be.mauricedeke.shinkai.data.local.room.dao.LexiconDao
import be.mauricedeke.shinkai.data.local.room.entity.LexiconEntryEntity
import be.mauricedeke.shinkai.data.remote.client.LexiconClient
import be.mauricedeke.shinkai.data.remote.client.VersionsClient
import be.mauricedeke.shinkai.data.remote.mapper.toDomain
import be.mauricedeke.shinkai.domain.model.LexiconEntry
import be.mauricedeke.shinkai.domain.repository.LexiconRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LexiconRepositoryImpl @Inject constructor(
    private val lexiconClient: LexiconClient,
    private val versionsClient: VersionsClient,
    private val appDataStore: AppDataStore,
    private val lexiconDao: LexiconDao
) : LexiconRepository {

    override suspend fun getLexiconEntries(): List<LexiconEntry>? {
        val apiVersion = versionsClient.getVersions().getOrNull()?.lexicon ?: -1
        val storedVersion = appDataStore.getLexiconVersion()
        val roomEntries = lexiconDao.getAll()

        if (apiVersion >= 0 && apiVersion == storedVersion && roomEntries.isNotEmpty()) {
            return roomEntries.map { it.toDomain() }
        }

        val dtos = lexiconClient.getLexiconEntries().getOrNull()
            ?: return roomEntries.takeIf { it.isNotEmpty() }?.map { it.toDomain() }

        val entries = dtos.map { it.toDomain() }

        lexiconDao.deleteAll()
        lexiconDao.upsertAll(dtos.map { dto ->
            LexiconEntryEntity(
                id = dto.id,
                japaneseWord = dto.japaneseWord,
                translation = dto.translation,
                description = dto.description
            )
        })

        if (apiVersion >= 0) appDataStore.setLexiconVersion(apiVersion)

        return entries
    }

    private fun LexiconEntryEntity.toDomain() = LexiconEntry(
        id = runCatching { UUID.fromString(id) }.getOrElse { UUID.randomUUID() },
        japaneseWord = japaneseWord,
        translation = translation,
        description = description
    )
}
