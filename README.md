# FoodScanner
> FoodScanner! The helpful ingredients app for all your recipe needs.

FoodScanner is an android app that helps you come up with recipes based on whatever ingredients you have lying around.

* Take pictures and use ✨machine learning✨ to determine the ingredients
* Manual input of ingredients and amount
* Step-by-step guide to creating said recipes

## Design

<img width="1032" alt="image" src="https://github.com/user-attachments/assets/f429963c-c13d-49a3-9bb8-70391ce2b024" />

## Dependencies
* [CameraX](https://developer.android.com/jetpack/androidx/releases/camera)
* [Tensorflow](https://ai.google.dev/edge/litert)
* [Retrofit](https://square.github.io/retrofit/)
* [Gson](https://github.com/google/gson)
* [Coil](https://github.com/coil-kt/coil)

## Jetpack Compose Features
* ViewModel
* Navigation

## Running the App

Visit our [App Website](https://staging.d2fhzzegitvejc.amplifyapp.com/)

1. The app does use a food API provided by RapidAPI so you will need an API key for that.
  - The [APK](https://staging.d2fhzzegitvejc.amplifyapp.com/static/media/app-release.c03141fd43f96a8ce273.apk) has the API lib embedded, but if you prefer to build it yourself
    1. Make sure you have CMake and NDK installed in Andriod Studio `Tools > SDK Manager > SDK Tools`
    2. Obtain the cpp file from Slack
    3. Rebuild the application 
3. Make sure you using Android 7.1 Nougat 🍫 or higher with at least API Level 25.

