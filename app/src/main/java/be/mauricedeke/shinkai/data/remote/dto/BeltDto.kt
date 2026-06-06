package be.mauricedeke.shinkai.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BeltDto(
    val name: String,
    val beltColor: String,
    val programme: ProgrammeDto,
    val technieken: List<TechniekDto>
)

@JsonClass(generateAdapter = true)
data class ProgrammeDto(
    val sections: List<ProgramSectionDto>
)

@JsonClass(generateAdapter = true)
data class ProgramSectionDto(
    val title: String,
    val items: List<String>
)

@JsonClass(generateAdapter = true)
data class TechniekDto(
    val name: String,
    val description: String,
    val belt: String? = null,
    val programme: String? = null
)
