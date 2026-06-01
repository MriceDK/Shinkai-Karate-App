package be.mauricedeke.shinkai.domain.repository

interface NoteRepository {
    suspend fun getNote(beltName: String): String
    suspend fun saveNote(beltName: String, note: String)
}
