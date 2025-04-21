# NeoPoolReward

**NeoPoolReward** is a small demonstration application showcasing the potential of Kotlin Multiplatform (KMP) for cross-platform mobile development. It highlights how shared business logic can be reused across Android and iOS platforms, improving maintainability and reducing code duplication.

## Features

- Kotlin Multiplatform architecture
- Shared business logic between Android and iOS
- Dependency injection with Koin (KMP-compatible)
- SwiftUI widget integration for iOS
- Jetpack Compose UI for Android

## Prerequisites

- **JDK 17** is required to build the project
- Android Studio Hedgehog or newer (recommended)
- Xcode

## Getting Started

### Android

To build and run the Android version:

1. Open the root project in **Android Studio**.
2. Let Gradle sync and resolve all dependencies.
3. Run the app on an emulator or physical device.

### iOS

To build and run the iOS version:

1. Open the `iosApp` subfolder project in **Xcode**.
2. In your target's **Build Phases**, add a new **Run Script Phase** with the following code:

    ```bash
    export JAVA_HOME=$(/usr/libexec/java_home -v 17)
    cd "$SRCROOT/.."
    chmod +x gradlew
    ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
    ```

3. This script ensures the Kotlin shared module is correctly built and embedded into the iOS app.

> 💡 For more detailed instructions on integrating Kotlin Multiplatform with an existing iOS application, refer to the official JetBrains documentation:  
[Make Your Cross-Platform Application Work on iOS](https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-integrate-in-existing-app.html#make-your-cross-platform-application-work-on-ios)

## Technologies Used

- Kotlin Multiplatform (KMP)
- Koin for dependency injection
- Jetpack Compose (Android)
- SwiftUI (iOS)
- Gradle (Android build system)

## Project Goals

The goal of NeoPoolReward is to:

- Demonstrate a simple yet effective Kotlin Multiplatform setup
- Explore DI and UI integration across platforms
- Serve as a reference project for KMP adoption in real-world apps

---

Feel free to explore, extend, and adapt the project for your own use cases.