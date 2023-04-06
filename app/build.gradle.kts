import com.dartlen.elevation.NiaBuildType

plugins {
    id("android.application")
    id("elv.android.application.compose")
    id("elv.android.application.flavors")
    //id("nowinandroid.android.application.jacoco")
    id("elv.android.hilt")
    //id("jacoco")
    //id("nowinandroid.android.application.firebase")
}

android {
    defaultConfig {
        applicationId = "com.dartlen.elevation"
        versionCode = 1
        versionName = "0.0.1" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        // Custom test runner to set up Hilt dependency graph
        testInstrumentationRunner = "com.dartlen.elevation.core.testing.NiaTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        val debug by getting {
            applicationIdSuffix = NiaBuildType.DEBUG.applicationIdSuffix
        }
        val release by getting {
            isMinifyEnabled = true
            applicationIdSuffix = NiaBuildType.RELEASE.applicationIdSuffix
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            // To publish on the Play store a private signing key is required, but to allow anyone
            // who clones the code to sign and run the release variant, use the debug signing key.
            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
            signingConfig = signingConfigs.getByName("debug")
        }
        val benchmark by creating {
            // Enable all the optimizations from release build through initWith(release).
            initWith(release)
            matchingFallbacks.add("release")
            // Debug key signing is available to everyone.
            signingConfig = signingConfigs.getByName("debug")
            // Only use benchmark proguard rules
            proguardFiles("benchmark-rules.pro")
            isMinifyEnabled = true
            applicationIdSuffix = NiaBuildType.BENCHMARK.applicationIdSuffix
        }
    }

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    namespace = "com.dartlen.elevation"
}

dependencies {
    implementation(project(":feature:current"))
    implementation(project(":feature:map"))
    implementation(project(":feature:history"))
    implementation(project(":feature:map"))
    implementation(project(":feature:settings"))

    implementation(project(":core:location"))


    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    //implementation(project(":core:analytics"))

    //implementation(project(":sync:work"))

//    androidTestImplementation(project(":core:testing"))
//    androidTestImplementation(project(":core:datastore-test"))
//    androidTestImplementation(project(":core:data-test"))
//    androidTestImplementation(project(":core:network"))
//    androidTestImplementation(libs.androidx.navigation.testing)
//    androidTestImplementation(libs.accompanist.testharness)
//    androidTestImplementation(kotlin("test"))
//    debugImplementation(libs.androidx.compose.ui.testManifest)
//    debugImplementation(project(":ui-test-hilt-manifest"))

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.lifecycle.runtimeCompose)
//    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(project(mapOf("path" to ":core:domain")))
//    implementation(libs.androidx.window.manager)
//    implementation(libs.androidx.profileinstaller)

//    implementation(libs.coil.kt)
}
