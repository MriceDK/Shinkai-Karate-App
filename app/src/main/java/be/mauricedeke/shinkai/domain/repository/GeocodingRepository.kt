package be.mauricedeke.shinkai.domain.repository

interface GeocodingRepository {
    suspend fun geocode(query: String): Pair<Double, Double>?
}
