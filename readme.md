# ShinKai: Karate Club App

A mobile companion app for members of a Shinkempo karate club. Track your training, keep up with club events, study techniques per belt, and measure your progress with built-in strength tests.

**Author:** Maurice De Kegel

[Download the APK](https://appdistribution.firebase.dev/i/b0d43bfb2c9e44fe) · [Source code](https://github.com/Howest-TI-Project-BnD/st-client-mobile-Maurice-De-Kegel)

---

## Features

**Events**
- Swipeable monthly calendar with an inbox for unanswered RSVPs
- RSVP to events, open the location in your maps app, or add the event to your calendar
- Reminders for upcoming events and trainings, with configurable lead time
- Geofencing: arriving at an event prompts you to log your attendance

**Training**
- Training history with a calendar view and per-training notes
- Technique and kata overview per belt level
- Searchable Japanese–Dutch lexicon

**Strength tests**
- Punch force, measured with the accelerometer
- Kiai volume, measured in dB with the microphone
- Best scores saved per belt

**Map**
- Mapbox map with live position, event markers and dojo zones
- Turn-by-turn navigation to an event

**Profile and settings**
- Belt display, profile picture (camera or gallery) and editable account details
- Customisable home screen shortcuts
- Per-type notification toggles, plus light/dark theme

**Live updates**
- Push notifications over a LavinMQ (AMQP) message broker, filtered by your notification settings

---

## Tech Stack

| Area | Technology |
|---|---|
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM, UiState, Use Cases, Repositories, Hilt |
| Networking | Retrofit + Moshi, SSL-pinned REST API |
| Local storage | Room, Preferences DataStore |
| Security | Android Keystore (AES-256-GCM) encrypts the JWT access token |
| Background work | WorkManager, foreground service |
| Messaging | LavinMQ via AMQP (consumer and publisher) |
| Maps and location | Mapbox (Maps + Navigation SDK), GPS, Geofencing API |
| Sensors | Accelerometer, microphone, camera |
| Testing | JUnit + MockK unit tests, instrumented Room DAO tests |
| CI/CD | Firebase App Distribution |

---

## Architecture

```
UI (Composables)
    └── ViewModel  ←→  UiState
            └── Use Cases
                    └── Repositories
                            ├── Remote (Retrofit + Moshi)
                            ├── Local (Room, DataStore)
                            └── Sensors (Accelerometer, Microphone, Location)
```

---

## Screens

| Area | Screens |
|---|---|
| Main tabs | Home, Events, Kaart (map), Technieken, Profiel |
| Events | Event detail, Manage events |
| Learning | Techniques, Kata list, Lexicon |
| Progress | Training history, Strength tests (punch force, kiai) |
| Settings | Account, Notifications, Location |
| Auth | Login |

---
