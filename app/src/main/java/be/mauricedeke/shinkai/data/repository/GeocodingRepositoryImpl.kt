package be.mauricedeke.shinkai.data.repository

import android.net.Uri
import be.mauricedeke.shinkai.domain.repository.GeocodingRepository
import com.mapbox.common.MapboxOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class GeocodingRepositoryImpl @Inject constructor() : GeocodingRepository {

    private val client = OkHttpClient()

    override suspend fun geocode(query: String): Pair<Double, Double>? {
        val token = MapboxOptions.accessToken
        val encoded = Uri.encode(query)
        val url = "https://api.mapbox.com/geocoding/v5/mapbox.places/$encoded.json" +
                "?access_token=$token&limit=1"

        return suspendCancellableCoroutine { cont ->
            val call = client.newCall(Request.Builder().url(url).build())
            call.enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: return cont.resume(null)
                        val features = JSONObject(body).getJSONArray("features")
                        if (features.length() == 0) return cont.resume(null)
                        val coords = features.getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONArray("coordinates")
                        cont.resume(Pair(coords.getDouble(1), coords.getDouble(0)))
                    } catch (e: Exception) {
                        cont.resume(null)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    cont.resume(null)
                }
            })
            cont.invokeOnCancellation { call.cancel() }
        }
    }
}
