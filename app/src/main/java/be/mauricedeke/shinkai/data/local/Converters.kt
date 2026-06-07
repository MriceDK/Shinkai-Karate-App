package be.mauricedeke.shinkai.data.local

import androidx.room.TypeConverter
import org.json.JSONArray
import java.util.UUID

class Converters {
    @TypeConverter
    fun fromUuid(uuid: UUID?): String? = uuid?.toString()

    @TypeConverter
    fun toUuid(value: String?): UUID? = value?.takeIf { it.isNotBlank() }?.let {
        runCatching { UUID.fromString(it) }.getOrNull()
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String = JSONArray(list).toString()

    @TypeConverter
    fun toStringList(value: String): List<String> = runCatching {
        val arr = JSONArray(value)
        (0 until arr.length()).map { arr.getString(it) }
    }.getOrElse { emptyList() }
}
