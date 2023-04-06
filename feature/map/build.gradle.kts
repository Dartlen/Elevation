plugins {
    id("elv.android.feature")
    id("elv.android.library.compose")
}

android {
    namespace = "com.dartlen.elevation.feature.map"
}

dependencies{
    implementation ("com.mapbox.maps:android:10.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.1.0")

    //implementation("com.mapbox.mapboxsdk:mapbox-android-sdk:10.12.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.appcompat:appcompat-resources:1.6.1")
}