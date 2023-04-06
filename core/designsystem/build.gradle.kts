plugins {
    id("elv.android.library")
    id("elv.android.library.compose")
    //id("nowinandroid.android.library.jacoco")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    lint {
        checkDependencies = true
    }
    namespace = "com.dartlen.elevation.core.designsystem"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.kt.compose)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    debugApi(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.runtime)

    api(project(":core:model"))

    //lintPublish(project(":lint"))
    //androidTestImplementation(project(":core:testing"))

    implementation ("com.patrykandpatrick.vico:compose-m3:1.6.4")
}