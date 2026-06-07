package be.mauricedeke.shinkai.ui.technieken

import be.mauricedeke.shinkai.domain.model.Belt
import be.mauricedeke.shinkai.domain.model.Kata

data class TechniekUiState(
    val belts: List<Belt> = emptyList(),
    val katas: List<Kata> = emptyList(),
    val isError: Boolean = false,
    val isKataError: Boolean = false
)
