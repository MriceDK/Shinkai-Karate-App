package be.mauricedeke.shinkai

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import be.mauricedeke.shinkai.ui.navigation.Screen
import be.mauricedeke.shinkai.ui.navigation.ShinkaiBottomBar
import be.mauricedeke.shinkai.ui.navigation.ShinkaiNavGraph
import be.mauricedeke.shinkai.ui.permissions.AppPermission
import be.mauricedeke.shinkai.ui.permissions.PermissionManager
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import be.mauricedeke.shinkai.ui.theme.ThemeViewModel

@Composable
fun ShinkaiApp() {
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val themeUiState by themeViewModel.uiState.collectAsStateWithLifecycle()
    val systemDarkTheme = isSystemInDarkTheme()
    val darkThemeEnabled = themeUiState.darkThemeEnabled ?: systemDarkTheme

    val mainViewModel: MainViewModel = hiltViewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val permissionRequest by mainViewModel.permissionRequest.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        mainViewModel.inAppNotification.collect { notification ->
            snackbarHostState.showSnackbar("${notification.title}: ${notification.body}")
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
        ) {
            mainViewModel.requestPermission(AppPermission.Notifications)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) mainViewModel.recheckPendingLog()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    PermissionManager(
        permissionRequest = permissionRequest,
        onPermissionResult = mainViewModel::onPermissionResult
    )

    ShinkaiAppContent(
        darkThemeEnabled = darkThemeEnabled,
        onDarkThemeToggle = themeViewModel::onDarkThemeToggle,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun ShinkaiAppContent(
    darkThemeEnabled: Boolean,
    onDarkThemeToggle: (Boolean) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    ShinkaikarateappTheme(darkTheme = darkThemeEnabled) {
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkThemeEnabled
            }
        }

        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val activeTab = when {
            currentRoute == null || currentRoute == Screen.Home.route -> Screen.Home.route
            currentRoute == Screen.Technieken.route || currentRoute.startsWith("technieken/") -> Screen.Technieken.route
            currentRoute == Screen.Events.route || currentRoute.startsWith("events/") -> Screen.Events.route
            currentRoute == Screen.Lexicon.route -> Screen.Lexicon.route
            else -> Screen.Profiel.route
        }

        val showBottomBar = currentRoute != Screen.Login.route

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                if (showBottomBar) {
                    ShinkaiBottomBar(
                        activeRoute = activeTab,
                        onTabClick = { route -> navController.navigate(route) }
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(snackbarData = data)
                }
            }
        ) { innerPadding ->
            ShinkaiNavGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                darkThemeEnabled = darkThemeEnabled,
                onDarkThemeToggle = onDarkThemeToggle
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_5)
@Composable
fun ShinkaiAppPreview() {
    ShinkaiAppContent(
        darkThemeEnabled = false,
        onDarkThemeToggle = {}
    )
}
