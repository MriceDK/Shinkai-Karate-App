package be.mauricedeke.shinkai.data.local

import androidx.room.TypeConverter
import java.util.UUID

class Converters {
    @TypeConverter
    fun fromUuid(uuid: UUID?): String? = uuid?.toString()

    @TypeConverter
    fun toUuid(value: String?): UUID? = value?.takeIf { it.isNotBlank() }?.let {
        runCatching { UUID.fromString(it) }.getOrNull()
    }
}
