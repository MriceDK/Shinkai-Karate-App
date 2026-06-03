# 🎬 Project Status — Howest Prime App

## Author
- Ann Audenaert  
- Koen Koreman  


# Project Summary

The mobile app is responsible for retrieving Howest Prime tickets and notifying the user about important events such as payment reminders and movie start reminders.

The app also contains extra features that improve the cinema experience for visitors entering the building.


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
- worked on other project

## Thursday, June 4th, 2026

... continue for each day until the end of the project

# Status Overview Technical Requirements

## Status Legend

| Status | Meaning |
|---|---|
| ✅ | Implemented |
| ⏳ | Work in progress |
| ❌ | Not implemented |
| 🧪 | Testing |
| ⚠️ | Problems / blocked |

## Must Have (12/20)

### Tickets

| Status | Feature | Notes |
|---|---|---|
| ⏳ | Receive tickets from the MessageBroker | |
| ❌ | Save tickets in a local Room database | |
| ❌ | Display upcoming tickets in a TicketListScreen | |
| ❌ | Show QR code when clicking on a ticket | |
| ❌ | Set a reminder notification 1 hour before movie start | |

### Snacks

| Status | Feature | Notes |
|---|---|---|
| ❌ | Retrieve snack data via Retrofit | |
| ❌ | Save snacks in a local Room database | |
| ❌ | Display snacks in a SnackListScreen | |
| ❌ | Add snacks to a cart | |
| ❌ | Show cart overview in CartScreen | |
| ❌ | Share order via intent/message | |

### Location

| Status | Feature | Notes |
|---|---|---|
| ❌ | Show Mapbox map with cinema location | |
| ❌ | Display user location marker | |


### Sensors

| Status | Feature | Notes |
|---|---|---|
| ❌ | Detect light intensity and movement | |
| ❌ | Flashlight activation via toggle button | |
| ❌ | Unit testing | |


## Intermediate (14+/20)

### Tickets

| Status | Feature | Notes |
|---|---|---|
| ❌ | Show detailed ticket information | |
| ❌ | Multiple notification options | |


### Snacks

| Status | Feature | Notes |
|---|---|---|
| ❌ | Send snack orders to MessageBroker | |


### Location

| Status | Feature | Notes |
|---|---|---|
| ❌ | Detect entering/exiting building | |


### Sensors

| Status | Feature | Notes |
|---|---|---|
| ❌ | Auto flashlight inside building | |
| ❌ | Auto flashlight when light is low | |
| ❌ | Auto flashlight on movement | |
| ❌ | Unit + instrumented testing | |


## Experienced (16+/20)

###  Tickets

| Status | Feature | Notes |
|---|---|---|
| ❌ | Save tickets securely in key vault | |
| ❌ | Filter upcoming/all tickets | |
| ❌ | Rate expired tickets | |

### Snacks

| Status | Feature | Notes |
|---|---|---|
| ❌ | Notification when order is ready | |
| ❌ | Locker system with QR-code | |
| ❌ | QR scanning with camera | |
| ❌ | Unlock locker after successful scan | |

### Location

| Status | Feature | Notes |
|---|---|---|
| ❌ | Navigate to correct room | |
| ❌ | Show friends’ locations | |


### Extra Features

| Status | Feature | Notes |
|---|---|---|
| ❌ | Additional approved features | |


## Extra Mile (18+/20)

| Status | Feature | Notes |
|---|---|---|
| ❌ | CI/CD deployment to Firebase App Distribution | |


# App Overview

Describe the implementation of the following topics.


## Screenshots
![](ReadmeImages/Screenshot.png)

Provide screenshots for every screen in the application.  
Each screen should have a unique name.


## Room Database
![](ReadmeImages/Database.png)

Describe:
- Stored data
- Related screens
- Database structure


##  API Requests
![](ReadmeImages/API.png)

Describe:
- API endpoint
- JSON response
- Screen usage

## MessageBroker
![](ReadmeImages/Database.png)

Describe:
- Connection setup
- Queue/topic usage
- Message handling


## Tickets
![](ReadmeImages/Notifications.png)

Describe:
- Ticket storage
- Ticket retrieval
- QR generation


## Intents
![](ReadmeImages/Intents.png)

Describe:
- Shared intents
- Navigation intents
- External app integration


## WorkManager
![](ReadmeImages/Workmanager.png)

Describe:
- Background tasks
- Scheduling
- Notification handling

## Notifications
![](ReadmeImages/Notifications.png)

Describe:
- Reminder notifications
- Notification channels
- Trigger moments


##  Map
![](ReadmeImages/SensorData.png)

Describe:
- Mapbox integration
- Geolocation
- GeoJSON usage


##  Sensor Data
![](ReadmeImages/SensorData.png)

Describe:
- Sensor usage
- Light detection
- Movement detection


##  Camera (Optional)
![](ReadmeImages/Camera.png)

Describe:
- QR scanning implementation
- Camera integration


# Repositories

## Code Repository
- [Link to repository]

##  APK
- [Link to APK]
