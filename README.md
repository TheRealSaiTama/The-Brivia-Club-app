# The Brivia Club App

A native Android app scaffold for The Brivia Club.

## Project structure
- `app/` — Android app module
- `.github/agents/android-app-maker.agent.md` — custom agent for Android development

## Brand direction
- Dark luxury theme
- Wine, gold, and charcoal palette
- Swipe card experience inspired by premium matchmaking apps

## How to build
This workspace currently does not include a valid Gradle wrapper or a local Java/Gradle install.

To build:
1. Install Java JDK 17
2. Install Android Studio or the Android SDK
3. Generate Gradle wrapper by running an installed Gradle instance in the project root:
   ```powershell
   gradle wrapper --gradle-version 8.4.1
   ```
4. Build with the wrapper:
   ```powershell
   .\gradlew :app:assembleDebug
   ```

## Notes
- The app uses Jetpack Compose and Material theming.
- Navigation is wired for onboarding, home, discover, and matches screens.
- UI components include premium branded cards and sample deck content.
