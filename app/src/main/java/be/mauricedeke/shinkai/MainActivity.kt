package be.mauricedeke.shinkai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import be.mauricedeke.shinkai.messaging.AmqpNotificationService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startForegroundService(Intent(this, AmqpNotificationService::class.java))
        enableEdgeToEdge()
        setContent {
            ShinkaiApp()
        }
    }
}
