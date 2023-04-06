plugins {
    id("elv.android.library")
    id("elv.android.hilt")
}

android {
    namespace = "com.dartlen.elevation.feature.location"
}
dependencies {

    implementation("com.google.android.gms:play-services-location:18.0.0")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("androidx.lifecycle:lifecycle-service:2.6.0")

    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))
    //implementation("pub.devrel:easypermissions:3.0.0")
}

