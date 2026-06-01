package be.mauricedeke.shinkai.ui.lexicon

import be.mauricedeke.shinkai.domain.model.LexiconEntry

data class LexiconUiState(
    val entries: List<LexiconEntry> = emptyList(),
    val searchQuery: String = ""
) {
    val filteredEntries: List<LexiconEntry> get() = entries.filter {
        searchQuery.isBlank() ||
        it.japaneseWord.contains(searchQuery, ignoreCase = true) ||
        it.translation.contains(searchQuery, ignoreCase = true)
    }
}
