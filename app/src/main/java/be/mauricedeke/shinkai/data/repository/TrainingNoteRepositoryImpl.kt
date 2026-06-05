package be.mauricedeke.shinkai.data.repository

import be.mauricedeke.shinkai.data.fake.FakeDataSource
import be.mauricedeke.shinkai.domain.repository.TrainingNoteRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainingNoteRepositoryImpl @Inject constructor() : TrainingNoteRepository {
    override fun getNote(id: UUID): String =
        FakeDataSource.trainings.firstOrNull { it.id == id }?.note ?: ""

    override fun saveNote(id: UUID, note: String) {
        val i = FakeDataSource.trainings.indexOfFirst { it.id == id }
        if (i >= 0) FakeDataSource.trainings[i] = FakeDataSource.trainings[i].copy(note = note)
    }
}
