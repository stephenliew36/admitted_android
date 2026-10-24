# Admitted

Admitted is an Android app for the college application journey. Students create a profile, track academics and activities, explore colleges, and keep applications in one place.

## Features

- **Roadmap** — courses, tests, honors, extracurriculars, and a readiness score
- **Explore** — search and save colleges
- **Consult** — guidance while building an application
- **Applications** — track the schools you are applying to

Sign-in supports email, Google, and Facebook.

## Requirements

- Android Studio with Android SDK 35
- JDK 17
- A Firebase project for package `com.realtouchapp.admitted`

The Gradle wrapper uses Gradle 8.13.

## Setup

API keys and signing credentials stay on your machine. Copy the examples and replace the placeholder values:

```bash
cp app/src/main/res/values/configue.example.xml app/src/main/res/values/configue.xml
cp app/google-services.json.example app/google-services.json
cp keystore.properties.example keystore.properties
```

- `configue.xml` holds the Google and Facebook client values and the Maps key.
- `google-services.json` is the Firebase config for this app.
- `keystore.properties` points at your upload keystore. A release build is signed only when that file is present.

Open the project in Android Studio and let Gradle sync.

## Build

```bash
./gradlew assembleDebug
```

Current version is 1.2.1 (version code 39). The app runs on Android 7.0 and newer (API 24).
