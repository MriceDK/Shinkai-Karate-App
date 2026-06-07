package be.mauricedeke.shinkai.geofence

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeofenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val client = LocationServices.getGeofencingClient(context)

    private val pendingIntent: PendingIntent by lazy {
        PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, GeofenceBroadcastReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    @SuppressLint("MissingPermission")
    fun setup(items: List<GeofenceItem>) {
        if (items.isEmpty()) return
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            Log.w("GeofenceManager", "Location permission not granted, skipping")
            return
        }

        // requestId encodes: name|date|startTime|endTime|logType
        val geofences = items.map { item ->
            Geofence.Builder()
                .setRequestId("${item.name}|${item.date}|${item.startTime}|${item.endTime}|${item.logType}")
                .setCircularRegion(item.lat, item.lng, 50f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build()
        }

        val request = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(geofences)
            .build()

        client.removeGeofences(pendingIntent).addOnCompleteListener {
            client.addGeofences(request, pendingIntent)
                .addOnSuccessListener { Log.d("GeofenceManager", "Registered ${items.size} geofences") }
                .addOnFailureListener { e -> Log.e("GeofenceManager", "Failed to register geofences", e) }
        }
    }

    fun clear() = client.removeGeofences(pendingIntent)
}
