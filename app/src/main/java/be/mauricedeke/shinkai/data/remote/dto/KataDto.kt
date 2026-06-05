package be.mauricedeke.shinkai.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KataDto(
    val id: String,
    val name: String,
    val belt: String,
    val beltColor: String?,
    val description: String,
    val moves: List<String>
)
