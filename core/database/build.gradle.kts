plugins {
    id("elv.android.library")
    id("elv.android.hilt")
    id("elv.android.room")
    id("kotlinx-serialization")
}

android {
    defaultConfig {
        testInstrumentationRunner =
            "com.google.samples.apps.nowinandroid.core.testing.NiaTestRunner"
    }
    namespace = "com.dartlen.elevation.core.database"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
}