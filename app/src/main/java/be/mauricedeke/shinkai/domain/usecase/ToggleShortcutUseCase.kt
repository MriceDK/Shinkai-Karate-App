package be.mauricedeke.shinkai.domain.usecase

import javax.inject.Inject

class ToggleShortcutUseCase @Inject constructor(
    private val updateShortcuts: UpdateShortcutsUseCase
) {
    suspend operator fun invoke(currentIds: List<String>, toggleId: String, maxShortcuts: Int): List<String> {
        val updated = currentIds.toMutableList()
        if (updated.contains(toggleId)) {
            updated.remove(toggleId)
        } else if (updated.size < maxShortcuts) {
            updated.add(toggleId)
        }
        updateShortcuts(updated)
        return updated
    }
}
