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
    3. Make a `cpp` folder in `app/src/main`
    4. Rebuild the application
        - Should look like this
  ```
├── AndroidManifest.xml
├── assets
│   └── model.tflite
├── cpp
│   └── api-key.cpp <-----
├── ic_launcher-playstore.png
├── java
│   └── com
│       └── zybooks
│           └── foodscanner
│               ├── ImageProcessor.kt
│               ├── MainActivity.kt
│               ├── data
│               │   ├── Ingredients.kt
│               │   ├── Recipe.kt
│               │   ├── RecipeAPIService.kt
│               │   └── RecipeDetails.kt
│               └── ui
│                   ├── AddIngredientsScreen.kt
│                   ├── AddViewModel.kt
│                   ├── CameraScreen.kt
│                   ├── CameraScreenViewModel.kt
│                   ├── DetailedRecipeScreen.kt
│                   ├── ImagePicker.kt
│                   ├── RecipeTablePage.kt
│                   ├── RecipeViewModel.kt
│                   └── theme
│                       ├── Color.kt
│                       ├── Theme.kt
│                       └── Type.kt
└── res
    ├── drawable
    │   ├── ic_launcher_background.xml
    │   └── ic_launcher_foreground.xml
    ├── mipmap-anydpi-v26
    │   ├── ic_launcher.xml
    │   └── ic_launcher_round.xml
    ├── mipmap-hdpi
    │   ├── ic_launcher.webp
    │   ├── ic_launcher_foreground.webp
    │   └── ic_launcher_round.webp
    ├── mipmap-mdpi
    │   ├── ic_launcher.webp
    │   ├── ic_launcher_foreground.webp
    │   └── ic_launcher_round.webp
    ├── mipmap-xhdpi
    │   ├── ic_launcher.webp
    │   ├── ic_launcher_foreground.webp
    │   └── ic_launcher_round.webp
    ├── mipmap-xxhdpi
    │   ├── ic_launcher.webp
    │   ├── ic_launcher_foreground.webp
    │   └── ic_launcher_round.webp
    ├── mipmap-xxxhdpi
    │   ├── ic_launcher.webp
    │   ├── ic_launcher_foreground.webp
    │   └── ic_launcher_round.webp
    ├── values
    │   ├── colors.xml
    │   ├── ic_launcher_background.xml
    │   ├── secrets.xml
    │   ├── strings.xml
    │   └── themes.xml
    └── xml
        ├── backup_rules.xml
        └── data_extraction_rules.xml
         
```

  
3. Make sure you using Android 7.1 Nougat 🍫 or higher with at least API Level 25.

