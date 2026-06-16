package be.mauricedeke.shinkai

import android.app.Application
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.mapbox.common.MapboxOptions
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class ShinkaiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initRemoteConfig()
    }

    private fun initRemoteConfig() {
        val rc = Firebase.remoteConfig
        rc.setConfigSettingsAsync(remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        })
        rc.setDefaultsAsync(R.xml.remote_config_defaults)

        try {
            // Block until Remote Config values are fetched and activated so that
            // all Hilt singletons (Retrofit, AMQP) get real values on first launch.
            // Runs on Firebase's background thread; we only wait here (no network on main thread).
            Tasks.await(rc.fetchAndActivate(), 5, TimeUnit.SECONDS)
        } catch (e: Exception) {
            // Fetch failed or timed out — continue with whatever cached values exist.
        }

        val token = rc.getString("mapbox_public_token")
        if (token.isNotEmpty()) MapboxOptions.accessToken = token
    }
}
