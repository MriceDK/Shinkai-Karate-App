# ShinKai — Karate Club App

## Author
- Maurice De Kegel

## Project Summary

ShinKai is a mobile companion app for members of a Shinkempo karate club. Members can manage their training history, track upcoming events, study techniques and lexicon per belt level, and measure their physical progress through strength tests. The app communicates with a REST API and a LavinMQ message broker, and persists user data locally with Room.

---

# Daily Status

## Sunday, June 1st, 2026
- Set up project: renamed package, configured build, added dependency catalog
- Added domain models, repositories, use cases and Hilt DI modules
- Wired all screens to ViewModels with UiState
- Set up Material 3 theme with custom app icon
- Made the events calendar work dynamically with the current date
- Added swipe left/right on the calendar to switch between months
- Added functionality to open events from the calendar (including past events)
- Made technieken per belt render dynamically from FakeDataSource
- Added BeltColor enum and all belt definitions

## Monday, June 2nd, 2026
- Added `localDate` and `rsvp` fields to the Event model, updated FakeDataSource with future-dated events
- Wired events and home screen to FakeDataSource, limited displayed events, added placeholders
- Added manage events screen with per-event RSVP, past events restricted
- Made repositories and use cases return nullable types to support API failure handling
- Added `isError` states and error UI across all features
- Persisted notification and location settings in Room instead of in-memory storage
- Wired manage events to the nav graph, scoped settings ViewModels to backstack entry

## Tuesday, June 3rd, 2026
- Fixed calendar: viewing a day with multiple events and styling
- Added profile picture selector (camera + gallery), persisted URI in Room
- Added punch force test (accelerometer) and kiai strength test (microphone)
- Persisted best strength test scores to Room, surfaced results on overview
- Added configurable notification preferences (per-type toggles + reminder timing)
- Fixed LavinMQ messaging: dynamic queue, exchange routing, per-user routing key
- Wired AMQP foreground service with WorkManager for push notifications
- Added UUID userId to UserProfile with Room type converter and migration
- Added shortcut editability on the home screen
- Added local functionality for logging trainings
- Added custom app icon and applied colour theme throughout

## Wednesday, June 4th, 2026
- Added Mapbox to the Kaart screen with event markers
- Added GeoJSON dojo zone polygons on the map
- Added navigation to an event location from the map
- Added centralised `PermissionManager`, removed inline permission launchers
- Extracted accelerometer access into `AccelerometerRepository`
- Extracted audio recording into `MicrophoneRepository`
- Added `LocationRepository` for live GPS position tracking
- Replaced Room theme storage with Preferences DataStore
- Updated camera/gallery profile picture chooser with a proper camera intent

## Thursday, June 5th, 2026
- Added login screen with authentication flow
- Created all remote DTOs for Auth, User, Events, Trainings, Belts, Katas, Lexicon, Strength, Support
- Created all Retrofit API interfaces for every endpoint
- Changed all string IDs to UUIDs across models, repositories, nav graph

## Friday, June 6th, 2026
- Wired all Retrofit API interfaces to their repository implementations
- Replaced FakeDataSource calls with live API data across all features

## Saturday, June 7th, 2026
- Fixed errors on first login
- Moved logic from ViewModels to use cases
- Added katas to the Technieken screen, added white belt colour
- Added pull-to-refresh on tabs that fetch from the API
- Fixed various API call bugs
- Fixed date formatting
- Added `ReminderScheduler` — a WorkManager-based background scheduler that locally schedules event and training reminder notifications based on the user's configured lead time, replacing server-pushed reminders
- Added support screen
- SSL-pinned the API
- Rendered belt correctly on the profile screen
- Fixed next training session retrieval
- Added upcoming trainings support

## Sunday, June 8th, 2026
- Added geofencing for events and training sessions (enter/exit triggers)
- Fixed loading of events from the API
- Implemented Android Keystore key vault: `KeyVaultManager` generates a hardware-backed AES-256-GCM key and encrypts the JWT access token before it is persisted to DataStore
- Added unit tests: `ComputeEventListsUseCase`, `ToggleShortcutUseCase`, `GetHomeUpcomingEventsUseCase`, `LoginViewModel`, `MainViewModel`
- Added instrumented tests: `NotificationSettingsDao`, `ShortcutsDao`, `UserProfileDao` (in-memory Room database)

---

# Rubric Status

## Status Legend

| Symbol | Meaning |
|---|---|
| ✅ | Done |
| ⚠️ | Partially done |
| ❌ | Not yet done |

---

## Must Have — 12/20

| Status | Requirement | Implementation |
|---|---|---|
| ✅ | Native UI — Jetpack Compose | All screens built in Compose |
| ✅ | Multi screen (min. 4) | 16+ screens with NavGraph |
| ✅ | Menu-based navigation | Bottom navigation bar (Home, Events, Kaart, Technieken, Profiel) |
| ✅ | Material Design with custom theme | Material 3 theme, custom colours, launcher icon |
| ✅ | Android App Architecture | ViewModel, UiState, Repository, Use Cases, Hilt DI throughout |
| ✅ | Room database | 7 local tables (user profile, settings, notes, shortcuts, scores, katas) |
| ✅ | Retrofit | REST API with 9 interfaces, Moshi JSON parsing |
| ✅ | WorkManager | `ReminderScheduler` + `NotificationWorker` for local background reminders |
| ✅ | 2 intents | `ACTION_VIEW` (geo: URI → maps app) + `ACTION_INSERT` (calendar) in EventDetailScreen |
| ✅ | Message broker | LavinMQ/AMQP via `AmqpNotificationService`, consumer + publisher |
| ✅ | GPS + Mapbox | Live user location on Kaart screen, event markers, dojo zone polygons |
| ✅ | 2 sensors | Accelerometer (punch force test) + Microphone (kiai dB test) |
| ✅ | Notifications | Local scheduled notifications via WorkManager, push via AMQP |
| ✅ | Tests — unit tests | 5 unit test classes covering use cases and ViewModels; 3 instrumented DAO tests with in-memory Room database |

---

## Intermediate — 14/20

| Status | Requirement | Implementation                                                                         |
|---|---|----------------------------------------------------------------------------------------|
| ✅ | Multiple notification channels | `shinkai_notifications` (reminders) + `shinkai_service` (foreground service)           |
| ✅ | Message broker — publish | `LavinMQMessagePublisher` publishes from the app                                       |
| ✅ | Geofencing | Dojo zones rendered as GeoJSON polygons on map, enter/exit triggers fire notifications |
| ✅ | Automatic sensor actions | Flashlight flashes when getting a notification                                         |
| ✅ | Camera | Profile picture capture via `ActivityResultContracts.TakePicture` in AccountScreen     |
| ✅ | Unit + instrumented tests | 5 unit test classes (use cases + ViewModels with MockK), 3 instrumented DAO tests with in-memory Room database |

---

## Experienced — 16/20

| Status | Requirement | Implementation                                                                                                                                                 |
|---|---|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ✅ | Key vault | `KeyVaultManager` wraps Android Keystore (AES-256-GCM, hardware-backed); JWT access token is encrypted before being written to DataStore and decrypted on read |
| ✅ | Filtering MessageBroker data | `isEnabled()` in `AmqpNotificationService` filters by message type and user notification settings                                                              |
| ✅ | GPS navigation | Mapbox Navigation SDK is a dependency and is used when navigating to a dynamically generated marker on the map for events or training sessions                 |

---

## Extra Mile — 18+/20

| Status | Requirement | Implementation |
|---|---|---|
| ❌ | CI/CD → Firebase App Distribution | No pipeline configured |

---

# TODO

Ordered by priority / effort:

1. **CI/CD** — GitHub Actions workflow that builds a signed APK and deploys to Firebase App Distribution

---

# Architecture

```
UI (Composables)
    └── ViewModel  ←→  UiState
            └── Use Cases
                    └── Repositories (interfaces)
                            ├── Remote (Retrofit + Moshi)
                            ├── Local (Room DAOs, DataStore)
                            └── Sensors (Accelerometer, Microphone, Location)
```

---

# Screens

| Screen | Description |
|---|---|
| LoginScreen | Email + password login, stores auth token |
| HomeScreen | Upcoming events, next training, customisable shortcuts |
| EventsScreen | Swipeable calendar, event list, inbox (unanswered RSVPs) |
| EventDetailScreen | Event info, RSVP, open in maps intent, add to calendar intent |
| ManageEventsScreen | RSVP for all upcoming events |
| KaartScreen | Mapbox map with event markers, dojo zones, live user location |
| TechniekScreen | Belt selector, techniques per belt, kata list |
| KataListScreen | Katas fetched from API grouped by belt |
| LexiconScreen | Searchable Japanese–Dutch glossary |
| ProfielScreen | Profile overview with belt, shortcuts, stats |
| AccountScreen | Edit name/email, set profile picture (camera or gallery) |
| TrainingHistoryScreen | Calendar + training log per day, per-training notes |
| StrengthTestScreen | Best punch force and kiai scores per belt |
| PunchTestScreen | Accelerometer-based punch force measurement |
| KiaiTestScreen | Microphone-based kiai dB measurement |
| NotificationsScreen | Toggle and configure notification preferences and reminder timing |
| LocationScreen | Toggle location permission usage |

---

# Repositories

## Code Repository
- [GitHub — st-client-mobile-Maurice-De-Kegel](https://github.com)

## APK
- [Link to APK]
