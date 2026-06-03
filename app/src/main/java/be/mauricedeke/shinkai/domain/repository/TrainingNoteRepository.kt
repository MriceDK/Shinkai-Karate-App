package be.mauricedeke.shinkai.domain.repository

interface TrainingNoteRepository {
    fun getNote(id: String): String
    fun saveNote(id: String, note: String)
}
