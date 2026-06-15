package be.mauricedeke.shinkai.data.remote

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateJsonAdapter {
    @ToJson
    fun toJson(value: LocalDate): String = value.format(DateTimeFormatter.ISO_LOCAL_DATE)

    @FromJson
    fun fromJson(value: String): LocalDate = LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
}

class LocalDateTimeJsonAdapter {
    @ToJson
    fun toJson(value: LocalDateTime): String = value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    @FromJson
    fun fromJson(value: String): LocalDateTime =
        LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}
