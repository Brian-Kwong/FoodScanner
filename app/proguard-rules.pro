# Keep Retrofit classes
-keep class retrofit2.** { *; }

# Keep Gson classes
-keep class com.google.gson.** { *; }

# Prevent obfuscation of annotations (important for Retrofit's annotation processor)
-keepclassmembers,allowobfuscation class * {
    @retrofit2.http.* <methods>;
}

# Keep fields with Gson annotations (e.g., @SerializedName)
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Preserve methods using TypeToken or generic types (important for Gson)
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class com.google.gson.internal.** { *; }

# Keep native methods if you use NDK code
-keepclasseswithmembers class * {
    native <methods>;
}

# Keep specific classes with native methods (example)
-keep class com.zybooks.foodscanner.APIKeyLibrary { *; }

# Keep generic signature of Call, Response (important for Retrofit)
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# With R8 full mode generic signatures are stripped for non-kept items
# Keep suspend function continuations with type arguments (important for Kotlin coroutines)
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Prevent data models from being obfuscated (important for Gson)
-keep class com.zybooks.foodscanner.data.** { *; }
