package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.remote.client.TrainingClient
import be.mauricedeke.shinkai.domain.repository.TrainingNoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainingNoteRepositoryImpl @Inject constructor(
    private val trainingClient: TrainingClient
) : TrainingNoteRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val notes = mutableMapOf<UUID, String>()

    fun seedNote(id: UUID, note: String) {
        if (!notes.containsKey(id)) notes[id] = note
    }

    override fun getNote(id: UUID): String = notes[id] ?: ""

    override fun saveNote(id: UUID, note: String) {
        notes[id] = note
        scope.launch {
            trainingClient.updateNote(id.toString(), note)
        }
    }
}
