plugins {
    id("elv.android.library")
    //id("nowinandroid.android.library.jacoco")
    id("elv.android.hilt")
}

android {
    namespace = "com.dartlen.elevation.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    //testImplementation(project(":core:testing"))
}