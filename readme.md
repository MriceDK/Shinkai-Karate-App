# ShinKai — Karate Club App

## Author
- Maurice De Kegel

## Project Summary

ShinKai is a mobile companion app for members of a Shinkempo karate club. It gives members a central place to manage their training, track upcoming events, study techniques and lexicon per belt level, and measure their physical progress through strength tests. The app communicates with a REST API and a LavinMQ message broker, and persists user data locally with Room.

---

# Daily Status

> Update this document **every day**:
> - Describe briefly what you did that day
> - Update the status tables for each feature

## Monday, June 1st, 2026
- Made the events and calendar work dynamically with the current date
- Added functionality for opening events from the calendar (also works for past events)
- Added swipe left/right on the calendar to switch between months

## Tuesday, June 2nd, 2026
- Added `localDate` and `rsvp` fields to the Event model and updated FakeDataSource with future-dated events
- Wired events and home screen to FakeDataSource, limited displayed events and added placeholders
- Added manage events screen with per-event RSVP and upcoming-only restriction
- Made repositories and use cases return nullable types to support API failure handling
- Added `isError` states and error UI across all features for graceful API failure handling
- Persisted notification and location settings in Room instead of in-memory storage
- Wired manage events to the nav graph and scoped settings ViewModels to backstack entry for save-on-click behavior

## Wednesday, June 3rd, 2026
- Worked on other project

## Thursday, June 4th, 2026
- Added camera/gallery chooser bottom sheet for profile picture in AccountScreen
- Fixed camera crash (SecurityException) by adding runtime CAMERA permission request
- Persisted captured photos to filesDir and saved URI in Room
- Made ProfielScreen observe Room reactively so profile picture updates immediately
- Added `java.util.UUID` IDs to all domain models (Event, Training, Techniek, LexiconEntry)
- Refactored all repositories, use cases, ViewModels, UiStates, screens, and NavGraph to use UUID
- Replaced rsvpMap pattern with direct mutation on mutable event lists

## Friday, June 5th, 2026
- Created all remote DTOs (`data/remote/dto/`) with Moshi `@JsonClass` annotations
- Created all Retrofit API interfaces (`data/remote/api/`) covering Auth, User, Events, Trainings, Belts, Katas, Lexicon, Strength, Support
- Created request body DTOs for all POST/PUT/PATCH endpoints

---

# Status Overview

## Status Legend

| Status | Meaning |
|---|---|
| ✅ | Implemented |
| ⏳ | In progress / wired to fake data |
| ❌ | Not yet implemented |

---

## Screens

| Status | Screen | Notes |
|---|---|---|
| ✅ | HomeScreen | Upcoming events, next training, customisable shortcuts |
| ✅ | EventsScreen | Calendar view, event list, inbox events |
| ✅ | EventDetailScreen | Event info, RSVP buttons |
| ✅ | ManageEventsScreen | RSVP management for all events, past events greyed out |
| ✅ | KaartScreen | Mapbox map with event markers, dojo zones (GeoJSON), user location |
| ✅ | TechniekScreen | Belt selector |
| ✅ | TechniekDetailScreen | Programme sections, technique list, notes per belt |
| ✅ | LexiconScreen | Searchable Japanese–Dutch glossary |
| ✅ | ProfielScreen | Profile overview, belt, stats |
| ✅ | AccountScreen | Edit name/email, profile picture (camera + gallery) |
| ✅ | TrainingHistoryScreen | Calendar + training log per day, notes per training |
| ✅ | StrengthTestScreen | Overview of best punch + kiai scores |
| ✅ | PunchTestScreen | Accelerometer-based punch force measurement |
| ✅ | KiaiTestScreen | Microphone-based kiai dB measurement |
| ✅ | NotificationsScreen | Toggle and configure notification preferences |
| ✅ | LocationScreen | Toggle location permission usage |
| ❌ | LoginScreen | Auth flow not yet implemented |
| ❌ | KataScreen | No screen yet |
| ❌ | SupportScreen | No screen yet |

---

## Data Layer

### Room Database

| Status | Table | Used by |
|---|---|---|
| ✅ | `user_profile` | ProfielScreen, AccountScreen — name, email, belt, profile picture URI |
| ✅ | `belt_note` | TechniekDetailScreen — free-text notes per belt |
| ✅ | `notification_settings` | NotificationsScreen — all notification toggle/reminder prefs |
| ✅ | `location_settings` | LocationScreen — location permission toggles |
| ✅ | `shortcuts` | HomeScreen — which shortcuts are pinned |
| ✅ | `strength_result` | StrengthTestScreen — best punch and kiai scores |

### Remote API (Retrofit + Moshi)

All interfaces and DTOs are defined. None are wired to repositories yet — all data still comes from FakeDataSource.

| Status | Interface | Endpoints |
|---|---|---|
| ⏳ | `AuthApi` | `POST /auth/login` |
| ⏳ | `UserApi` | `GET /users/me`, `PUT /users/me` |
| ⏳ | `EventApi` | `GET /events`, `GET /events/inbox`, `GET /events/{id}`, `POST /events/{id}/rsvp` |
| ⏳ | `TrainingApi` | `GET /trainings`, `GET /trainings/next`, `GET /trainings/{id}`, `POST /trainings`, `PATCH /trainings/{id}/note` |
| ⏳ | `BeltApi` | `GET /belts`, `GET /belts/{name}`, `GET /belts/{name}/notes`, `PUT /belts/{name}/notes` |
| ⏳ | `KataApi` | `GET /katas` |
| ⏳ | `LexiconApi` | `GET /lexicon` |
| ⏳ | `StrengthApi` | `GET /strength-results`, `PUT /strength-results/{type}`, `POST /strength-test/punch`, `POST /strength-test/kiai` |
| ⏳ | `SupportApi` | `POST /support` |

### Message Broker (LavinMQ)

| Status | Feature | Notes |
|---|---|---|
| ✅ | `LavinMQMessageConsumer` | Connects to broker, consumes messages |
| ✅ | `LavinMQMessagePublisher` | Publishes messages to broker |
| ✅ | `AmqpNotificationService` | Listens for push notification events |
| ✅ | `NotificationEventBus` | In-app event bus for broker messages |

---

## Sensors & Hardware

| Status | Feature | Notes |
|---|---|---|
| ✅ | Accelerometer | Used in PunchTestScreen to measure strike force |
| ✅ | Microphone | Used in KiaiTestScreen to measure kiai volume in dB |
| ✅ | GPS / FusedLocationProvider | Used in KaartScreen to show user position on map |
| ✅ | Camera | Used in AccountScreen to take a new profile photo |
| ✅ | Gallery | Used in AccountScreen to pick a profile photo |

---

## Background Work

| Status | Feature | Notes |
|---|---|---|
| ✅ | `NotificationWorker` | WorkManager worker for scheduled reminders |
| ✅ | `WorkManagerModule` | Hilt module providing WorkManager |

---

## Other

| Status | Feature | Notes |
|---|---|---|
| ✅ | Dark / Light theme | Toggled from settings, persisted via DataStore |
| ✅ | Permission manager | Central `PermissionManager` for CAMERA, location, microphone |
| ✅ | Bottom navigation | Home, Events, Kaart, Technieken, Profiel |
| ✅ | Hilt dependency injection | All repositories, use cases, ViewModels wired |
| ✅ | MVVM + Use Cases | Clean Architecture throughout |
| ❌ | Login / token auth | Bearer token not yet attached to Retrofit requests |
| ❌ | Real API wiring | Repositories still use FakeDataSource |

---

# App Overview

## Architecture

```
UI (Composables)
    └── ViewModel  ←→  UiState
            └── Use Cases
                    └── Repositories (interfaces)
                            ├── Impl (FakeDataSource / Room / Retrofit)
                            └── Data layer (Room DAOs, Retrofit APIs, DataStore)
```

## Room Database

Six tables are persisted locally:
- **user_profile** — single-row table (id = 0) for the logged-in user's name, email, belt, and profile picture URI
- **belt_note** — one note string per belt name for the Technieken screen
- **notification_settings** — all reminder and notification toggles
- **location_settings** — location permission usage toggles
- **shortcuts** — ordered list of shortcut IDs pinned on the Home screen
- **strength_result** — best punch (N) and kiai (dB) scores

## API Requests

All endpoints are defined in `data/remote/api/`. JSON is parsed with Moshi. Request and response DTOs live in `data/remote/dto/`. The next step is a Hilt `NetworkModule` that provides Retrofit instances, followed by replacing FakeDataSource calls in each `RepositoryImpl`.

## Message Broker

LavinMQ (AMQP) is used for real-time push notifications. `LavinMQMessageConsumer` connects and consumes messages; `AmqpNotificationService` translates them into Android notifications via `NotificationEventBus`.

## WorkManager

`NotificationWorker` handles scheduled background reminders (event and training reminders) triggered from `NotificationSettings`. Scheduling logic lives in `WorkerUtils`.

## Map

Mapbox SDK is used on the Kaart screen. Event locations are shown as markers (lat/lng from the API). Dojo zones are rendered as GeoJSON polygons. The user's live GPS position is tracked via `LocationRepository` using the Fused Location Provider.

## Sensors

- **Accelerometer** (`AccelerometerRepository`) — records peak G-force during a punch and maps the score to a `BeltColor` via `BeltPunchScore`
- **Microphone** (`MicrophoneRepository`) — records audio, computes peak dB, maps to a `BeltColor` via `BeltKiaiDb`

## Camera

Profile picture changes in `AccountScreen` offer a bottom sheet with two options:
- **Camera** — requests `CAMERA` permission, uses `FileProvider` to create a temp file, copies the result to `filesDir/profile_pictures/` after capture
- **Gallery** — uses `PickVisualMedia` with `takePersistableUriPermission`

The selected URI is saved immediately to Room and observed reactively so `ProfielScreen` updates without requiring a manual refresh.

---

# Repositories

## Code Repository
- [GitHub — st-client-mobile-Maurice-De-Kegel](https://github.com)

## APK
- [Link to APK]
