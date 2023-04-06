plugins {
    id("elv.android.feature")
    id("elv.android.library.compose")
}

android {
    namespace = "com.dartlen.elevation.feature.current"
}
dependencies {
    implementation("androidx.compose.animation:animation-graphics:1.1.1")
    implementation("com.google.accompanist:accompanist-permissions:0.28.0")

    implementation(project(mapOf("path" to ":core:common")))
    implementation(project(":core:location"))


}
