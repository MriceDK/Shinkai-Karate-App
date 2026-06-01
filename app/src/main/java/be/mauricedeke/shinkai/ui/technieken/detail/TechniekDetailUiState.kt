package be.mauricedeke.shinkai.ui.technieken.detail

import be.mauricedeke.shinkai.domain.model.Techniek

data class TechniekDetailUiState(
    val belt: String = "",
    val technieken: List<Techniek> = emptyList(),
    val notes: String = ""
)
