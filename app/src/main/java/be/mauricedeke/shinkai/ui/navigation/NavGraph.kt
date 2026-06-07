package be.mauricedeke.shinkai.ui.navigation

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import be.mauricedeke.shinkai.MainViewModel
import be.mauricedeke.shinkai.ui.permissions.AppPermission
import java.util.UUID
import be.mauricedeke.shinkai.ui.events.EventsScreen
import be.mauricedeke.shinkai.ui.events.EventsViewModel
import be.mauricedeke.shinkai.ui.events.detail.EventDetailScreen
import be.mauricedeke.shinkai.ui.events.detail.EventDetailViewModel
import be.mauricedeke.shinkai.ui.events.manage.ManageEventsScreen
import be.mauricedeke.shinkai.ui.events.manage.ManageEventsViewModel
import be.mauricedeke.shinkai.ui.home.HomeScreen
import be.mauricedeke.shinkai.ui.home.HomeViewModel
import be.mauricedeke.shinkai.ui.kaart.KaartScreen
import be.mauricedeke.shinkai.ui.kaart.KaartViewModel
import be.mauricedeke.shinkai.ui.lexicon.LexiconScreen
import be.mauricedeke.shinkai.ui.lexicon.LexiconViewModel
import be.mauricedeke.shinkai.ui.profiel.ProfielScreen
import be.mauricedeke.shinkai.ui.profiel.ProfielViewModel
import be.mauricedeke.shinkai.ui.profiel.account.AccountScreen
import be.mauricedeke.shinkai.ui.profiel.account.AccountViewModel
import be.mauricedeke.shinkai.ui.profiel.history.TrainingHistoryScreen
import be.mauricedeke.shinkai.ui.profiel.history.TrainingHistoryViewModel
import be.mauricedeke.shinkai.ui.profiel.location.LocationScreen
import be.mauricedeke.shinkai.ui.profiel.location.LocationViewModel
import be.mauricedeke.shinkai.ui.profiel.notifications.NotificationsScreen
import be.mauricedeke.shinkai.ui.profiel.notifications.NotificationsViewModel
import be.mauricedeke.shinkai.ui.profiel.strength.StrengthTestScreen
import be.mauricedeke.shinkai.ui.profiel.strength.StrengthTestViewModel
import be.mauricedeke.shinkai.ui.profiel.strength.kiai.KiaiTestScreen
import be.mauricedeke.shinkai.ui.profiel.strength.kiai.KiaiTestViewModel
import be.mauricedeke.shinkai.ui.profiel.strength.punch.PunchTestScreen
import be.mauricedeke.shinkai.ui.profiel.strength.punch.PunchTestViewModel
import be.mauricedeke.shinkai.ui.technieken.TechniekScreen
import be.mauricedeke.shinkai.ui.technieken.TechniekViewModel
import be.mauricedeke.shinkai.ui.technieken.detail.TechniekDetailScreen
import be.mauricedeke.shinkai.ui.technieken.detail.TechniekDetailViewModel

@Composable
fun ShinkaiNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    darkThemeEnabled: Boolean,
    onDarkThemeToggle: (Boolean) -> Unit,
    mainViewModel: MainViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    techniekViewModel: TechniekViewModel = hiltViewModel(),
    techniekDetailViewModel: TechniekDetailViewModel = hiltViewModel(),
    eventsViewModel: EventsViewModel = hiltViewModel(),
    eventDetailViewModel: EventDetailViewModel = hiltViewModel(),
    manageEventsViewModel: ManageEventsViewModel = hiltViewModel(),
    lexiconViewModel: LexiconViewModel = hiltViewModel(),
    kaartViewModel: KaartViewModel = hiltViewModel(),
    profielViewModel: ProfielViewModel = hiltViewModel(),
    strengthTestViewModel: StrengthTestViewModel = hiltViewModel(),
    trainingHistoryViewModel: TrainingHistoryViewModel = hiltViewModel(),
    accountViewModel: AccountViewModel = hiltViewModel(),
    loginViewModel: be.mauricedeke.shinkai.ui.login.LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val permissionRequest by mainViewModel.permissionRequest.collectAsStateWithLifecycle()
    val isAuthenticated by mainViewModel.isAuthenticated.collectAsStateWithLifecycle()

    LaunchedEffect(isAuthenticated) {
        when (isAuthenticated) {
            true -> navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
            false -> if (navController.currentDestination?.route != Screen.Login.route) {
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            null -> Unit
        }
    }

    val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val techniekUiState by techniekViewModel.uiState.collectAsStateWithLifecycle()
    val techniekDetailUiState by techniekDetailViewModel.uiState.collectAsStateWithLifecycle()
    val eventsUiState by eventsViewModel.uiState.collectAsStateWithLifecycle()
    val eventDetailUiState by eventDetailViewModel.uiState.collectAsStateWithLifecycle()
    val manageEventsUiState by manageEventsViewModel.uiState.collectAsStateWithLifecycle()
    val lexiconUiState by lexiconViewModel.uiState.collectAsStateWithLifecycle()
    val kaartUiState by kaartViewModel.uiState.collectAsStateWithLifecycle()
    val profielUiState by profielViewModel.uiState.collectAsStateWithLifecycle()
    val strengthTestUiState by strengthTestViewModel.uiState.collectAsStateWithLifecycle()
    val trainingHistoryUiState by trainingHistoryViewModel.uiState.collectAsStateWithLifecycle()
    val accountUiState by accountViewModel.uiState.collectAsStateWithLifecycle()
    val loginUiState by loginViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(loginUiState.loginSuccess) {
        if (loginUiState.loginSuccess) {
            loginViewModel.onLoginHandled()
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            Screen.Home.route -> homeViewModel.load()
            Screen.Events.route -> eventsViewModel.refresh()
            Screen.ManageEvents.route -> manageEventsViewModel.refresh()
            Screen.TrainingHistory.route -> trainingHistoryViewModel.resetToToday()
            Screen.Technieken.route -> techniekViewModel.load()
            Screen.Lexicon.route -> lexiconViewModel.load()
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {

        composable(Screen.Login.route) {
            be.mauricedeke.shinkai.ui.login.LoginScreen(
                uiState = loginUiState,
                onEmailChanged = loginViewModel::onEmailChanged,
                onPasswordChanged = loginViewModel::onPasswordChanged,
                onLoginClick = loginViewModel::onLoginClick,
                onGuestClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                uiState = homeUiState,
                onNavigate = { navController.navigate(it) },
                onEventClick = { id ->
                    eventDetailViewModel.loadEvent(id)
                    navController.navigate(Screen.EventDetail.createRoute(id.toString()))
                },
                onShortcutToggle = { homeViewModel.toggleShortcut(it) }
            )
        }

        composable(Screen.Technieken.route) {
            TechniekScreen(
                uiState = techniekUiState,
                onBeltClick = { belt ->
                    techniekDetailViewModel.loadBelt(belt)
                    navController.navigate(Screen.TechniekDetail.createRoute(belt))
                }
            )
        }

        composable(
            Screen.TechniekDetail.route,
            arguments = listOf(navArgument("belt") { type = NavType.StringType })
        ) { backStack ->
            val belt = backStack.arguments?.getString("belt") ?: "Geel"
            techniekDetailViewModel.loadBelt(belt)
            TechniekDetailScreen(
                uiState = techniekDetailUiState,
                onNotesChanged = techniekDetailViewModel::onNotesChanged,
                onNotesFocusLost = techniekDetailViewModel::onNotesFocusLost,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Events.route) {
            EventsScreen(
                uiState = eventsUiState,
                onDateSelected = eventsViewModel::onDateSelected,
                onEventClick = { id ->
                    eventDetailViewModel.loadEvent(id)
                    navController.navigate(Screen.EventDetail.createRoute(id.toString()))
                },
                onManageEventsClick = { navController.navigate(Screen.ManageEvents.route) },
                onRsvp = eventsViewModel::setRsvp
            )
        }

        composable(Screen.ManageEvents.route) {
            ManageEventsScreen(
                uiState = manageEventsUiState,
                onRsvp = manageEventsViewModel::setRsvp,
                onBackClick = { navController.popBackStack() },
                onEventClick = { id ->
                    eventDetailViewModel.loadEvent(id)
                    navController.navigate(Screen.EventDetail.createRoute(id.toString()))
                }
            )
        }

        composable(
            Screen.EventDetail.route,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("eventId")
                ?.let { runCatching { UUID.fromString(it) }.getOrNull() }
            if (id != null) eventDetailViewModel.loadEvent(id)
            EventDetailScreen(
                uiState = eventDetailUiState,
                onBackClick = { navController.popBackStack() },
                onRsvp = eventDetailViewModel::setRsvp
            )
        }

        composable(Screen.Lexicon.route) {
            LexiconScreen(
                uiState = lexiconUiState,
                onSearchQueryChanged = lexiconViewModel::onSearchQueryChanged
            )
        }

        composable(Screen.Profiel.route) {
            ProfielScreen(
                uiState = profielUiState,
                onEditClick = { navController.navigate(Screen.Account.route) },
                onStrengthTestClick = { navController.navigate(Screen.StrengthTest.route) },
                onTrainingHistoryClick = { navController.navigate(Screen.TrainingHistory.route) },
                onNotificationsClick = { navController.navigate(Screen.Notifications.route) },
                onLocationClick = { navController.navigate(Screen.Location.route) },
                onLogoutClick = {
                    profielViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                isDarkTheme = darkThemeEnabled,
                onDarkThemeToggle = onDarkThemeToggle
            )
        }

        composable(Screen.Account.route) {
            AccountScreen(
                uiState = accountUiState,
                onNaamChanged = accountViewModel::onNaamChanged,
                onEmailChanged = accountViewModel::onEmailChanged,
                onCurrentPasswordChanged = accountViewModel::onCurrentPasswordChanged,
                onNewPasswordChanged = accountViewModel::onNewPasswordChanged,
                onConfirmPasswordChanged = accountViewModel::onConfirmPasswordChanged,
                onSavePassword = accountViewModel::onSavePassword,
                onSave = { accountViewModel.onSave(); navController.popBackStack() },
                onBackClick = { navController.popBackStack() },
                onProfilePictureSelected = accountViewModel::onProfilePictureSelected
            )
        }

        composable(Screen.TrainingHistory.route) {
            TrainingHistoryScreen(
                uiState = trainingHistoryUiState,
                onDateSelected = trainingHistoryViewModel::onDateSelected,
                onTrainingSelected = trainingHistoryViewModel::onTrainingSelected,
                onNotesChanged = trainingHistoryViewModel::onNotesChanged,
                onNotesFocusLost = trainingHistoryViewModel::onNotesFocusLost,
                onLogClick = trainingHistoryViewModel::showLogSheet,
                onLogSave = trainingHistoryViewModel::logTraining,
                onLogDismiss = trainingHistoryViewModel::dismissLogSheet,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.StrengthTest.route) {
            StrengthTestScreen(
                uiState = strengthTestUiState,
                onStartTest = { type ->
                    when {
                        type.contains("Punch", ignoreCase = true) -> navController.navigate(Screen.PunchTest.route)
                        type.contains("Kiai", ignoreCase = true) -> navController.navigate(Screen.KiaiTest.route)
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.PunchTest.route) {
            val vm: PunchTestViewModel = hiltViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            PunchTestScreen(
                uiState = uiState,
                onStart = vm::onStart,
                onReset = vm::onReset,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.KiaiTest.route) {
            val vm: KiaiTestViewModel = hiltViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            val micGranted = remember(permissionRequest) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
            }
            KiaiTestScreen(
                uiState = uiState,
                micPermissionGranted = micGranted,
                onRequestMicPermission = { mainViewModel.requestPermission(AppPermission.RecordAudio) },
                onStart = vm::onStart,
                onReset = vm::onReset,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Notifications.route) {
            val vm: NotificationsViewModel = hiltViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            NotificationsScreen(
                uiState = uiState,
                onSettingsChanged = vm::onSettingsChanged,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Location.route) {
            val vm: LocationViewModel = hiltViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            LocationScreen(
                uiState = uiState,
                onSettingsChanged = vm::onSettingsChanged,
                onSave = { vm.onSave(); navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Kaart.route) {
            val locationGranted = remember(permissionRequest) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            }
            KaartScreen(
                uiState = kaartUiState,
                locationPermissionGranted = locationGranted,
                onRequestLocationPermission = { mainViewModel.requestPermission(AppPermission.Location) },
                onLocationStart = kaartViewModel::onLocationStart,
                onLocationStop = kaartViewModel::onLocationStop,
                onEventSelected = kaartViewModel::onEventSelected,
                onFetchRoute = { dest -> kaartViewModel.fetchRoute(dest) },
                onViewEventDetails = { id ->
                    eventDetailViewModel.loadEvent(id)
                    navController.navigate(Screen.EventDetail.createRoute(id.toString()))
                }
            )
        }
    }
}
