package be.mauricedeke.shinkai

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import be.mauricedeke.shinkai.ui.navigation.Screen
import be.mauricedeke.shinkai.ui.navigation.ShinkaiBottomBar
import be.mauricedeke.shinkai.ui.navigation.ShinkaiNavGraph
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import be.mauricedeke.shinkai.ui.theme.ThemeViewModel

@Composable
fun ShinkaiApp() {
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val themeUiState by themeViewModel.uiState.collectAsStateWithLifecycle()
    val systemDarkTheme = isSystemInDarkTheme()
    val darkThemeEnabled = themeUiState.darkThemeEnabled ?: systemDarkTheme

    ShinkaiAppContent(
        darkThemeEnabled = darkThemeEnabled,
        onDarkThemeToggle = themeViewModel::onDarkThemeToggle
    )
}

@Composable
private fun ShinkaiAppContent(
    darkThemeEnabled: Boolean,
    onDarkThemeToggle: (Boolean) -> Unit
) {
    ShinkaikarateappTheme(darkTheme = darkThemeEnabled) {
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

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                ShinkaiBottomBar(
                    activeRoute = activeTab,
                    onTabClick = { route -> navController.navigate(route) }
                )
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
