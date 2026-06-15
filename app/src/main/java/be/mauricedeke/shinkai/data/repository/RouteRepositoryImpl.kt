package be.mauricedeke.shinkai.data.repository

import android.content.Context
import be.mauricedeke.shinkai.domain.repository.RouteRepository
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import dagger.hilt.android.qualifiers.ApplicationContext
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
class RouteRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : RouteRepository {

    private val client = OkHttpClient()

    override suspend fun fetchRoute(origin: Point, destination: Point): LineString? {
        val token = MapboxOptions.accessToken
        val coords = "${origin.longitude()},${origin.latitude()}" +
                ";${destination.longitude()},${destination.latitude()}"
        val url = "https://api.mapbox.com/directions/v5/mapbox/driving/$coords" +
                "?access_token=$token&geometries=geojson&overview=full"

        return suspendCancellableCoroutine { cont ->
            val call = client.newCall(Request.Builder().url(url).build())
            call.enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: return cont.resume(null)
                        val routes = JSONObject(body).getJSONArray("routes")
                        if (routes.length() == 0) return cont.resume(null)
                        val geomJson = routes.getJSONObject(0)
                            .getJSONObject("geometry").toString()
                        cont.resume(LineString.fromJson(geomJson))
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
