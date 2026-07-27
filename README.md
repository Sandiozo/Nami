# Mental Wellness Mobile App - Nami

An Android mental wellness application developed using Kotlin and Jetpack Compose.

The app provides affirmations, breathing exercises, cognitive behavioural therapy activities and guided wellbeing sessions. Users can customise their experience, create personal affirmations and receive scheduled affirmation notifications.

This project was developed as part of the MOB3000 mobile application development course.

## Overview

The application is designed to give users simple tools for supporting their mental wellbeing in everyday life.

Users can:

* Read positive affirmations
* Create custom affirmations
* Complete breathing exercises
* Access cognitive behavioural therapy activities
* Start guided wellbeing sessions
* Configure affirmation and notification preferences
* Receive affirmations as Android notifications

User settings and affirmations are stored locally using a Room database.

## Features

### Home Screen

The home screen presents affirmations and provides access to the main sections of the application.

Default affirmations can be enabled or disabled through the user settings.

### Affirmations

The app contains two types of affirmations:

* Default affirmations included with the application
* Custom affirmations created by the user

Default and custom affirmations are stored separately in the local database.

Users can choose whether default affirmations should appear:

* On the home screen
* In scheduled notifications

### Custom Affirmations

Users can create and store their own affirmation messages.

Custom affirmations are saved locally and can be included together with the default affirmations when displaying content or sending notifications.

### Scheduled Notifications

The application can send affirmation notifications at a user-selected interval.

An Android background worker retrieves the available affirmations from the Room database and displays one through the Android notification system.

Notification behaviour depends on the user's saved preferences.

### Breathing Exercises

The breathing section guides users through controlled breathing exercises intended to support relaxation and stress management.

### CBT Activities

The cognitive behavioural therapy section provides structured exercises intended to help users examine thoughts, emotions and behavioural patterns.

The app is not intended to replace professional mental health treatment.

### Guided Sessions

Users can access guided wellbeing sessions through a dedicated session screen.

### User Settings

The settings screen allows users to configure:

* Username
* Default affirmations on the home screen
* Default affirmations in notifications
* Notification interval
* Other personal application preferences

The settings are stored locally and restored when the app is opened again.

## Local Database

Room is used to store user settings and affirmations locally.

The main database tables are:

| Table         | Purpose                                   |
| ------------- | ----------------------------------------- |
| `user`        | Stores user preferences and settings      |
| `default_aff` | Stores affirmations included with the app |
| `custom_aff`  | Stores affirmations created by the user   |

### User Data

The user table stores settings such as:

* Username
* Whether default affirmations are shown on the home screen
* Whether default affirmations are included in notifications
* Selected notification interval


## ViewModel

The `UserViewModel` manages the current user state and communicates with the Room database.

Its responsibilities include:

* Loading the current user
* Updating the username
* Updating home-screen affirmation preferences
* Updating notification affirmation preferences
* Updating the notification interval
* Controlling the username dialog state

The UI observes the ViewModel state and automatically updates when stored values change.

## Affirmation Notifications

Scheduled notifications are handled by `AffirmationWorker`.

The worker:

1. Opens the Room database.
2. Loads the current user settings.
3. Retrieves default affirmations when enabled.
4. Retrieves custom affirmations.
5. Combines the available affirmation lists.
6. Selects an affirmation.
7. Displays it using the Android notification system.


## Privacy

The application stores user settings and affirmations locally on the device using Room.

The current version does not require an online account or remote server to store this data.

Users should avoid entering highly sensitive personal or medical information into the application.

