@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "HuaRongDao"
            isStatic = true
        }
    }

    jvm("desktop")

    wasmJs {
        outputModuleName.set("huarongdao")
        browser {
            commonWebpackConfig {
                outputFileName = "huarongdao.js"
            }
        }
        binaries.executable()
    }

    // Use the default hierarchy template but extend it with our own
    // 'nonWebMain' group covering Android + JVM(desktop) + iOS.
    // Room/SQLite have no wasmJs artifacts, so they cannot live in commonMain.
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        common {
            group("nonWeb") {
                withAndroidTarget()
                withJvm()   // desktop
                withIos()
            }
        }
    }

    sourceSets {
        val desktopMain by getting
        // 'nonWebMain' is created by the hierarchy template above.
        val nonWebMain by getting

        androidMain.dependencies {
            implementation(libs.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            implementation(libs.runtime)
            implementation(libs.jetbrains.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.jetbrains.components.resources)
            implementation(libs.ui.tooling.preview)
            implementation(libs.material.icons.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }

        // Room + SQLite + DataStore only on targets that publish those artifacts
        nonWebMain.dependencies {
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.datastore.preferences)
        }

        desktopMain.dependencies {
            // Dispatchers.Main 在 Desktop 上需要 Swing 的事件循环来驱动，没有这个依赖就找不到 Main dispatcher
            implementation(libs.kotlinx.coroutines.swing)
            implementation(compose.desktop.currentOs)
        }
    }
}

val keystorePropertiesFile = rootProject.file("local.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(keystorePropertiesFile.inputStream())
}

android {
    namespace = "com.mammoth.huarongdao"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.mammoth.huarongdao"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        create("release") {
            storeFile = rootProject.file(keystoreProperties["signing.storeFile"] ?: "")
            storePassword = keystoreProperties["signing.storePassword"] as String?
            keyAlias = keystoreProperties["signing.keyAlias"] as String?
            keyPassword = keystoreProperties["signing.keyPassword"] as String?
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            val releaseSigningConfig = signingConfigs.findByName("release")
            if (releaseSigningConfig != null && releaseSigningConfig.storeFile?.exists() == true) {
                signingConfig = releaseSigningConfig
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    applicationVariants.all {
        if (buildType.name == "release") {
            outputs.all {
                val output = this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
                output.outputFileName = "HuaRongDao-KMP.apk"
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.mammoth.huarongdao.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "HuaRongDao-KMP"
            packageVersion = "1.0.0"

            macOS {
                bundleID = "com.mammoth.huarongdao"
                dockName = "HuaRongDao-KMP"
                // iconFile.set(project.file("icon.icns")) // Ensure you have an .icns file

                // Required for distributing outside the App Store
                signing {
                    // identity.set("Developer ID Application: Your Name (ID)")
                }
                notarization {
                    // appleID.set("your@apple.id")
                    // password.set(project.findProperty("appleAppSpecificPassword")?.toString())
                    // teamID.set("YOUR_TEAM_ID")
                }
            }
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

// KSP for Room — wasmJs excluded (no Room support)
dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspDesktop", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
}
