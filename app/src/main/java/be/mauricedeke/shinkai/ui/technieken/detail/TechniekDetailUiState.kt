package be.mauricedeke.shinkai.ui.technieken.detail

import be.mauricedeke.shinkai.domain.model.ProgramSection
import be.mauricedeke.shinkai.domain.model.Techniek

data class TechniekDetailUiState(
    val belt: String = "",
    val program: List<ProgramSection> = emptyList(),
    val technieken: List<Techniek> = emptyList(),
    val notes: String = ""
)
