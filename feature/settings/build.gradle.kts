import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    id("elv.android.feature")
    id("elv.android.library.compose")
    //id("elv.android.library.jacoco")
}

android {
    namespace = "com.dartlen.elevation.feature.settings"
}
