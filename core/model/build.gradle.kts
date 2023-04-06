plugins {
    id("kotlin")
    id("kotlinx-serialization")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies{
    implementation(libs.kotlinx.serialization.json)
}