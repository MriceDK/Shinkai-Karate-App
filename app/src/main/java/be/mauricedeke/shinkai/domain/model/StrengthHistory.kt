package be.mauricedeke.shinkai.domain.model

data class StrengthHistory(
    val id: Int,
    val type: String,
    val score: Int,
    val unit: String,
    val beltColor: BeltColor?,
    val timestamp: Long
)
