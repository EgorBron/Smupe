# Building the app

## Prerequisites

* JDK 17
* Android SDK, API 34 or newer is required
* (optional) A real or emulated device with at least Android 7.0 (API 24) to test build
* (optional, highly recommended) Android Studio or IntelliJ IDEA with Android plugin
* (optional) A Git client

## Using CLI and Gradle

Follow theese steps if you don't have installed IntelliJ/Andoid Studio and want to build `Smupe!` using Gradle CLI.

1. Verify installation of JDK before you begin:

    ```sh
    $ java --version
    openjdk 17.x.x 20xx-xx-xx
    ```

2. Clone the repo using Git:

    ```sh
    git clone https://github.com/EgorBron/Smupe
    cd Smupe
    ```

3. Create a `local.properties` file in the root of the project, and fill it with the path to your Android SDK:

    ```properties
    sdk.dir=path/to/android/sdk
    ```

4. Verify installation of Android SDK and Gradle by performing debug build:

    ```sh
    $ gradlew assembleDebug
    ...
    BUILD SUCCESSFUL in ...
    ```

5. If build succeeds, the app will be available in `./app/build/outputs/apk/foss/debug/app-debug.apk`

## Using Android Studio or IntelliJ IDEA

If you prefer to use IDE to build, follow theese steps:

1. Open IDE and clone project by clicking `Get from VCS`
2. Select `Repository URL` -> `Git` and enter the repo URL
3. Click `Clone` and wait when project load
4. Android plugin will show an Android SDK selector pop-up message, where you should select installed Andoid SDK. If there's nothing, follow instructions to install correct SDK.
5. After that, click on `Build` > `Build APKs`
6. If build succeeds, click to `locate` in notification. The APK will be available in `debug` directory
