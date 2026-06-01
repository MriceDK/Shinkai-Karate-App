package be.mauricedeke.shinkai.domain.model

data class ProgramSection(
    val title: String,
    val items: List<String> = emptyList()
)
