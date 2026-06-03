package be.mauricedeke.shinkai.domain.model

data class StrengthResult(
    val type: String = "",
    val score: Int = 0,
    val beltColor: BeltColor? = null,
    val unit: String = ""
)
