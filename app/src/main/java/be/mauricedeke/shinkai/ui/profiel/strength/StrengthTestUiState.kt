package be.mauricedeke.shinkai.ui.profiel.strength

import be.mauricedeke.shinkai.domain.model.StrengthResult

data class StrengthTestUiState(
    val results: List<StrengthResult> = emptyList(),
    val isError: Boolean = false
)
