package be.mauricedeke.shinkai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import be.mauricedeke.shinkai.data.worker.EXTRA_DEEP_LINK_ROUTE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val pendingDeepLink = mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        pendingDeepLink.value = intent?.getStringExtra(EXTRA_DEEP_LINK_ROUTE)
        setContent {
            ShinkaiApp(deepLinkRoute = pendingDeepLink)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        pendingDeepLink.value = intent.getStringExtra(EXTRA_DEEP_LINK_ROUTE)
    }
}
