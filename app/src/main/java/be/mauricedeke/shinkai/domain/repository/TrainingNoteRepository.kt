package be.mauricedeke.shinkai.domain.repository

import java.util.UUID

interface TrainingNoteRepository {
    fun getNote(id: UUID): String
    fun saveNote(id: UUID, note: String)
}
