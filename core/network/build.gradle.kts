plugins {
    //id("com.android.library")
    //id("org.jetbrains.kotlin.android")
    id("elv.android.library")
    id("elv.android.hilt")
    id("kotlinx-serialization")
    //id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.dartlen.elevation.core.network"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}