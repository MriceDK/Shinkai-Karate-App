package be.mauricedeke.shinkai

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.mapbox.common.MapboxOptions
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShinkaiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initMapboxToken()
    }

    private fun initMapboxToken() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        })
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        // Activate any previously fetched values immediately (reads from local cache, no network)
        remoteConfig.activate().addOnSuccessListener {
            val token = remoteConfig.getString("mapbox_public_token")
            if (token.isNotEmpty()) MapboxOptions.accessToken = token
        }

        // Fetch fresh values from Firebase in the background for the next launch
        remoteConfig.fetch()
    }
}
