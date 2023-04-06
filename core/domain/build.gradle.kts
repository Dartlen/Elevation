plugins {
    //id("com.android.library")
    //id("org.jetbrains.kotlin.android")
    id("elv.android.library")
    kotlin("kapt")
}

android {
    namespace = "com.dartlen.elevation.core.domain"
}

dependencies {

    implementation(project(":core:data"))
    implementation(project(":core:model"))

    implementation(libs.kotlinx.coroutines.android)


    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}