package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.local.room.entity.BeltNoteEntity
import be.mauricedeke.shinkai.data.local.room.dao.NoteDao
import be.mauricedeke.shinkai.data.remote.client.BeltClient
import be.mauricedeke.shinkai.domain.repository.NoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val beltClient: BeltClient
) : NoteRepository {

    override suspend fun getNote(beltName: String): String {
        val local = noteDao.getNote(beltName)
        if (local != null) return local
        val remote = beltClient.getBeltNote(beltName).getOrNull()?.note ?: return ""
        if (remote.isNotEmpty()) noteDao.upsertNote(BeltNoteEntity(beltName, remote))
        return remote
    }

    override suspend fun saveNote(beltName: String, note: String) {
        noteDao.upsertNote(BeltNoteEntity(beltName, note))
        beltClient.updateBeltNote(beltName, note)
    }
}
