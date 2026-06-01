package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.local.BeltNoteEntity
import be.mauricedeke.shinkai.data.local.NoteDao
import be.mauricedeke.shinkai.domain.repository.NoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class  NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override suspend fun getNote(beltName: String): String =
        noteDao.getNote(beltName) ?: ""

    override suspend fun saveNote(beltName: String, note: String) =
        noteDao.upsertNote(BeltNoteEntity(beltName, note))
}
