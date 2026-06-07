package be.mauricedeke.shinkai.ui.permissions

sealed class AppPermission {
    data object Notifications : AppPermission()
    data object Location : AppPermission()
    data object BackgroundLocation : AppPermission()
    data object Camera : AppPermission()
    data object RecordAudio : AppPermission()
}
