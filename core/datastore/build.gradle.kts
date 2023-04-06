import com.google.protobuf.gradle.*

// TODO: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
//@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("elv.android.library")
    //id("nowinandroid.android.library.jacoco")
    id("elv.android.hilt")
    //alias(libs.plugins.protobuf)
    id("com.google.protobuf")

}

android {
//    defaultConfig {
//        consumerProguardFiles("consumer-proguard-rules.pro")
//    }
    namespace = "com.dartlen.elevation.core.datastore"

}

// Setup protobuf configuration, generating lite Java and Kotlin classes
protobuf {

    protoc {
        artifact = "com.google.protobuf:protoc:3.20.1"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                val java by registering {
                    option("lite")
                }
                val kotlin by registering {
                    option("lite")
                }
            }
        }
    }

}


dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    //testImplementation(project(":core:testing"))
    //testImplementation(project(":core:datastore-test"))

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.dataStore.core)
//    implementation(libs.protobuf.kotlin.lite)
//
//    implementation("com.google.protobuf:protobuf-kotlin:3.21.2")
//    implementation("com.google.protobuf:protobuf-java-util:3.21.7")
    implementation("com.google.protobuf:protobuf-kotlin-lite:3.22.2")

}