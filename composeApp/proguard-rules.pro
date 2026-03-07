# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/wuhaoyong/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.

# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Compose Multiplatform / Android
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

# Missing classes reported in logs
-dontwarn androidx.test.platform.app.InstrumentationRegistry
-dontwarn androidx.window.extensions.**
-dontwarn androidx.window.sidecar.**

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Kotlin Serialization
-keepattributes *Annotation*,InnerClasses
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}
-keepclassmembers class **$serializer {
    public static final ** INSTANCE;
}
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    @kotlinx.serialization.Transient *;
}

# Koin
-keep class org.koin.** { *; }
-dontwarn org.koin.**
