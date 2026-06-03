package be.mauricedeke.shinkai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
    accountViewModel: AccountViewModel = hiltViewModel()
) {
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

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                uiState = homeUiState,
                onNavigate = { navController.navigate(it) },
                onEventClick = { id ->
                    eventDetailViewModel.loadEvent(id)
                    navController.navigate(Screen.EventDetail.createRoute(id))
                }
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
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Events.route) {
            EventsScreen(
                uiState = eventsUiState,
                onDateSelected = eventsViewModel::onDateSelected,
                onEventClick = { id ->
                    eventDetailViewModel.loadEvent(id)
                    navController.navigate(Screen.EventDetail.createRoute(id))
                },
                onManageEventsClick = { navController.navigate(Screen.ManageEvents.route) }
            )
        }

        composable(Screen.ManageEvents.route) {
            ManageEventsScreen(
                uiState = manageEventsUiState,
                onRsvp = manageEventsViewModel::setRsvp,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            Screen.EventDetail.route,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("eventId") ?: ""
            eventDetailViewModel.loadEvent(id)
            EventDetailScreen(
                uiState = eventDetailUiState,
                onBackClick = { navController.popBackStack() }
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
                isDarkTheme = darkThemeEnabled,
                onDarkThemeToggle = onDarkThemeToggle
            )
        }

        composable(Screen.Account.route) {
            AccountScreen(
                uiState = accountUiState,
                onNaamChanged = accountViewModel::onNaamChanged,
                onEmailChanged = accountViewModel::onEmailChanged,
                onNewPasswordChanged = accountViewModel::onNewPasswordChanged,
                onConfirmPasswordChanged = accountViewModel::onConfirmPasswordChanged,
                onSavePassword = accountViewModel::onSave,
                onSave = { accountViewModel::onSave; navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.TrainingHistory.route) {
            TrainingHistoryScreen(
                uiState = trainingHistoryUiState,
                onDateSelected = trainingHistoryViewModel::onDateSelected,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.StrengthTest.route) {
            StrengthTestScreen(
                uiState = strengthTestUiState,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Notifications.route) {
            val vm: NotificationsViewModel = hiltViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            NotificationsScreen(
                uiState = uiState,
                onSettingsChanged = vm::onSettingsChanged,
                onSave = { vm.onSave(); navController.popBackStack() },
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
            KaartScreen(uiState = kaartUiState)
        }
    }
}
