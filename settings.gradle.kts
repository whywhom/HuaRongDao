rootProject.name = "HuaRongDao"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        // Alpha/snapshot androidx artifacts (Room 2.7+, SQLite 2.5+)
        maven("https://androidx.dev/snapshots/builds/12691961/artifacts/repository")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        // Alpha/snapshot androidx artifacts (Room 2.7+, SQLite 2.5+)
        maven("https://androidx.dev/snapshots/builds/12691961/artifacts/repository")
        google()
        mavenCentral()
    }
}

include(":composeApp")
