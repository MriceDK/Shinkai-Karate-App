package be.mauricedeke.shinkai.domain.usecase

import be.mauricedeke.shinkai.domain.repository.TrainingNoteRepository
import javax.inject.Inject

class GetTrainingNoteUseCase @Inject constructor(
    private val trainingNoteRepository: TrainingNoteRepository
) {
    operator fun invoke(id: String): String = trainingNoteRepository.getNote(id)
}
