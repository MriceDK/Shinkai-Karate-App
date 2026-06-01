package be.mauricedeke.shinkai.ui.technieken

import be.mauricedeke.shinkai.domain.model.Belt

data class TechniekUiState(
    val belts: List<Belt> = emptyList()
)
