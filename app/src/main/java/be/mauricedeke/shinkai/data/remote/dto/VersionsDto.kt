package be.mauricedeke.shinkai.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VersionsDto(
    val belts: Map<String, Int>,   // belt name (e.g. "YELLOW") → version
    val katas: Map<String, Int>,   // kata UUID → version
    val lexicon: Int               // single version for the whole lexicon
)
