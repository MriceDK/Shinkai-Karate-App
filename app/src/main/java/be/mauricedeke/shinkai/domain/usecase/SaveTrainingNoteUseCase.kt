package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.repository.TrainingNoteRepository
import javax.inject.Inject

class SaveTrainingNoteUseCase @Inject constructor(
    private val trainingNoteRepository: TrainingNoteRepository
) {
    operator fun invoke(id: java.util.UUID, note: String) =
        trainingNoteRepository.saveNote(id, note)
}
