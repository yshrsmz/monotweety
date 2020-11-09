includeBuild("includedBuild/dependencies")
includeBuild("includedBuild/build-helper")

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.android.application", "com.android.library" ->
                    useModule("com.android.tools.build:gradle:${requested.version}")
                "com.google.gms.google-services" ->
                    useModule("com.google.gms:google-services:${requested.version}")
                "com.google.firebase.crashlytics" ->
                    useModule("com.google.firebase:firebase-crashlytics-gradle:${requested.version}")
                "androidx.navigation.safeargs.kotlin" ->
                    useModule("androidx.navigation:navigation-safe-args-gradle-plugin:${requested.version}")
                "dagger.hilt.android.plugin" ->
                    useModule("com.google.dagger:hilt-android-gradle-plugin:${requested.version}")
                "com.google.android.gms.oss-licenses-plugin" ->
                    useModule("com.google.android.gms:oss-licenses-plugin:${requested.version}")
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

include(
    ":di-common",
    ":data",
    ":app",
    ":app2"
)
