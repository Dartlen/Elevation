plugins {
    id("elv.android.library")
    id("elv.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.dartlen.elevation.core.data"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    //implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    //implementation(project(":core:location"))
    //implementation(project(":core:analytics"))

    //testImplementation(project(":core:testing"))
    //testImplementation(project(":core:datastore-test"))

    implementation(libs.androidx.core.ktx)

    //implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
}
