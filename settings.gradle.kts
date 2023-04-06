pluginManagement {
    repositories {
        includeBuild("build-logic")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                // Do not change the username below.
                // This should always be `mapbox` (not your username).
                username = "mapbox"
                // Use the secret token you stored in gradle.properties as the password
                password = "pk.eyJ1IjoiZGFydGxlbiIsImEiOiJjbGFmdGt2bmowbnZrM3BwcTMwZXdoZXdyIn0.q0DctWm2bZiqp5cDwUSIkQ"
            }
        }
    }
}
rootProject.name = "Elevation"
include(":app")

include(":core")
include(":feature")

include(":feature:current")
include(":feature:settings")
include(":feature:history")
include(":feature:map")

include(":core:data")
include(":core:domain")
include(":core:database")
include(":core:network")
include(":core:model")
include(":core:ui")
include(":core:location")
include(":core:designsystem")
include(":core:datastore")
include(":core:common")

