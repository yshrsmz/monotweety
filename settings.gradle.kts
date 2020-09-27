include(":app2")
include(":data")
pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.android.application", "com.android.library" -> useModule("com.android.tools.build:gradle:${requested.version}")
                "com.google.gms.google-services" -> useModule("com.google.gms:google-services:${requested.version}")
                "com.google.firebase.crashlytics" -> useModule("com.google.firebase:firebase-crashlytics-gradle:${requested.version}")
                "androidx.navigation.safeargs.kotlin" -> useModule("androidx.navigation:navigation-safe-args-gradle-plugin:${requested.version}")
            }
        }
    }
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        jcenter()
    }
}

include(":data", ":app", ":app2")
