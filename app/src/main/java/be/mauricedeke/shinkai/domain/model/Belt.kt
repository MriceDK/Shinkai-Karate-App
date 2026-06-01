package be.mauricedeke.shinkai.domain.model

data class Belt(
    val name: String = "",
    val colorHex: String = "#FFD700",
    val pogramma: BeltProgram = BeltProgram(),
    val technieken: List<Techniek> = emptyList(),
    val lexiconEntries: List<LexiconEntry> = emptyList(),
    val notes : String = ""
)
