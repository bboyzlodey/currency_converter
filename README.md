# Currency converter

[![Kotlin Version](https://img.shields.io/badge/Kotlin-1.4-blue.svg)](https://kotlinlang.org)

A simple currency conversion application. A network request for a free resource [open.exchangerate-api](https://open.exchangerate-api.com/v6/latest/) is used. The result will be saved to local storage (database). The database is refreshed when the data is out of date (the timestamp of the next data refresh on the server is stored).



## Project characteristics and tech-stack

<img src="/app_screen.png" width="336" align="right" hspace="20">


* Tech-stack
    * [100% Kotlin](https://kotlinlang.org/) + [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - perform background operations
    * [Retrofit](https://square.github.io/retrofit/) - networking
    * [Jetpack](https://developer.android.com/jetpack)
        * [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) - in-app navigation
        * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - notify views about database change
        * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - perform an action when lifecycle state changes
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and manage UI-related data in a lifecycle conscious way
        * [Room](https://developer.android.com/jetpack/androidx/releases/room) - store offline cache
    * [Hilt](https://medium.com/androiddevelopers/dependency-injection-on-android-with-hilt-67b6031e62d) - dependency injection
    * [Moshi](https://github.com/square/moshi) - JSON converter
* Modern Architecture
    * Single activity architecture ( with [Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started))
    * MVVM (presentation layer)
    * [Android Architecture components](https://developer.android.com/topic/libraries/architecture) ([ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel), [LiveData](https://developer.android.com/topic/libraries/architecture/livedata), [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation))
    * [Android KTX](https://developer.android.com/kotlin/ktx) - Jetpack Kotlin extensions
* UI
    * [Material design](https://material.io/design)
