package be.mauricedeke.shinkai.ui.permissions

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun PermissionManager(
    permissionRequest: AppPermission?,
    onPermissionResult: () -> Unit
) {
    val notificationsLauncher = rememberLauncherForActivityResult(RequestPermission()) {
        onPermissionResult()
    }
    val locationLauncher = rememberLauncherForActivityResult(RequestMultiplePermissions()) {
        onPermissionResult()
    }
    val cameraLauncher = rememberLauncherForActivityResult(RequestPermission()) {
        onPermissionResult()
    }
    val recordAudioLauncher = rememberLauncherForActivityResult(RequestPermission()) {
        onPermissionResult()
    }

    LaunchedEffect(permissionRequest) {
        when (permissionRequest) {
            AppPermission.Notifications -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationsLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    onPermissionResult()
                }
            }
            AppPermission.Location -> locationLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
            AppPermission.Camera -> cameraLauncher.launch(Manifest.permission.CAMERA)
            AppPermission.RecordAudio -> recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
            null -> {}
        }
    }
}
