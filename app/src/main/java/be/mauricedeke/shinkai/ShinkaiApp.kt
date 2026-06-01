package be.mauricedeke.shinkai

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import be.mauricedeke.shinkai.ui.components.ShinkaiDrawerContent
import be.mauricedeke.shinkai.ui.navigation.Screen
import be.mauricedeke.shinkai.ui.navigation.ShinkaiBottomBar
import be.mauricedeke.shinkai.ui.navigation.ShinkaiNavGraph
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import kotlinx.coroutines.launch

@Composable
fun ShinkaiApp() {
    val systemDarkTheme = isSystemInDarkTheme()
    var darkThemeEnabled by remember { mutableStateOf(systemDarkTheme) }

    ShinkaikarateappTheme(darkTheme = darkThemeEnabled) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        val showBack = currentRoute != null && currentRoute !in listOf(
            Screen.Home.route, Screen.Technieken.route, Screen.Events.route,
            Screen.Lexicon.route, Screen.Profiel.route, Screen.Kaart.route
        )

        val activeTab = when {
            currentRoute == null || currentRoute == Screen.Home.route -> Screen.Home.route
            currentRoute == Screen.Technieken.route || currentRoute.startsWith("technieken/") -> Screen.Technieken.route
            currentRoute == Screen.Events.route || currentRoute.startsWith("events/") -> Screen.Events.route
            currentRoute == Screen.Lexicon.route -> Screen.Lexicon.route
            else -> Screen.Profiel.route
        }

        val title = when {
            currentRoute == null -> "Home"
            currentRoute == Screen.Home.route -> "Home"
            currentRoute == Screen.Technieken.route -> "Technieken"
            currentRoute.startsWith("technieken/") -> "Technieken"
            currentRoute == Screen.Events.route -> "Events"
            currentRoute.startsWith("events/") -> "Event"
            currentRoute == Screen.Lexicon.route -> "Lexicon"
            currentRoute == Screen.Profiel.route -> "Profiel"
            currentRoute == Screen.Account.route -> "Account"
            currentRoute == Screen.TrainingHistory.route -> "Training History"
            currentRoute == Screen.StrengthTest.route -> "Strength Test"
            currentRoute == Screen.Notifications.route -> "Notifications"
            currentRoute == Screen.Location.route -> "Location"
            currentRoute == Screen.Kaart.route -> "Kaart"
            else -> "Shinkai"
        }

        val subtitle = if (currentRoute?.startsWith("technieken/") == true)
            navBackStackEntry?.arguments?.getString("belt")
        else null

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ShinkaiDrawerContent(
                    onClose = { scope.launch { drawerState.close() } },
                    onNavigate = { route ->
                        navController.navigate(route)
                        scope.launch { drawerState.close() }
                    }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) {
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
                    onDarkThemeToggle = { darkThemeEnabled = it }
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_5)
@Composable
fun ShinkaiAppPreview() {
    ShinkaiApp()
}
