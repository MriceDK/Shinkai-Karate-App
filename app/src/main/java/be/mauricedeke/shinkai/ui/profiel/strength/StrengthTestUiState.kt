package be.mauricedeke.shinkai.ui.profiel.strength

import be.mauricedeke.shinkai.domain.model.StrengthHistory
import be.mauricedeke.shinkai.domain.model.StrengthResult

data class StrengthTestUiState(
    val results: List<StrengthResult> = emptyList(),
    val history: List<StrengthHistory> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
