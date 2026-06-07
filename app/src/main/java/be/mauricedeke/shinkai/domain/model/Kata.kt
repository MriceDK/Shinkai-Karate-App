package be.mauricedeke.shinkai.domain.model

data class Kata(
    val id: String,
    val name: String,
    val belt: String,
    val beltColor: BeltColor,
    val description: String,
    val moves: List<String>
)
