//apply plugin: "com.android.application"
//apply plugin: "kotlin-android"
//apply plugin: "kotlin-kapt"
////apply plugin: "com.getkeepsafe.dexcount"
//apply plugin: "com.google.gms.google-services"
//apply plugin: "com.google.firebase.crashlytics"
import java.util.Properties

plugins {
    id(Plugins.Ids.androidApp)
    kotlin("android")
    kotlin("kapt")
//    id("com.getkeepsafe.dexcount")
    id(Plugins.Ids.crashlytics)
    id(Plugins.Ids.googleservices)
    id(Plugins.Ids.navSafeArgs)
    id(Plugins.Ids.licenses)
    id("scabbard.gradle") version "0.4.0"
}

apply(from = rootProject.file("gradle/jacoco.gradle"))

fun getSecretProperty(name: String): String {
    val properties = Properties()
    properties.load(project.rootProject.file("secret.properties").inputStream())

    return properties.getProperty(name.toString())
}

android {

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("debug.keystore")
            storePassword = "android"
            keyPassword = "android"
        }
        create("release") {
            storeFile = rootProject.file(getSecretProperty("keystoreFile"))
            storePassword = getSecretProperty("keystorePassword")
            keyAlias = getSecretProperty("keyAlias")
            keyPassword = getSecretProperty("keyPassword")
        }
    }

    defaultConfig {
        applicationId = "net.yslibrary.monotweety"
        versionCode = 58
        versionName = "1.9.0"
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
            multiDexEnabled = true
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isZipAlignEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.4"
    }
}

dependencies {
    implementation(project(Deps.Modules.diCommon))
    implementation(project(Deps.Modules.data))

    implementation(Deps.Kotlin.Stdlib.jdk8)
    implementation(Deps.Kotlin.Coroutines.core)
    implementation(Deps.Kotlin.Coroutines.android)
    implementation(Deps.Kotlin.Coroutines.rx2)
    implementation(Deps.Kotlin.datetime)

    implementation(Deps.Androidx.appcompat)
    implementation(Deps.Androidx.browser)
    implementation(Deps.Androidx.recyclerview)
    implementation(Deps.Androidx.constraintlayout)
    implementation(Deps.Androidx.multidex)
    implementation(Deps.Androidx.ktx)
    implementation(Deps.Androidx.fragmentKtx)
    implementation(Deps.Androidx.Lifecycle.runtime)
    implementation(Deps.Androidx.Lifecycle.service)
    implementation(Deps.Androidx.Lifecycle.commonJava8)
    implementation(Deps.Androidx.Navigation.fragment)
    implementation(Deps.Androidx.Navigation.ui)
    implementation(Deps.Androidx.viewmodel)
    implementation(Deps.Androidx.Navigation.fragment)
    implementation(Deps.Androidx.Navigation.ui)

    implementation(Deps.material)

    implementation(Deps.licenses)

    implementation(platform(Deps.Firebase.bom))
    implementation(Deps.Firebase.analytics)
    implementation(Deps.Firebase.crashlytics)

    implementation(Deps.twitterText)
    implementation(Deps.Twitter4kt.core)

    implementation(Deps.OkHttp3.core)

    implementation(Deps.Dagger.core)
    kapt(Deps.Dagger.compiler)

    implementation(Deps.coil)

    implementation(Deps.timber)

    implementation(Deps.licenseAdapter)

    implementation(Deps.progressButton)

    implementation(Deps.groupie)

    implementation(Deps.FlowBinding.platform)
    implementation(Deps.FlowBinding.material)

    implementation(Deps.LeakCanary.plumber)
    debugImplementation(Deps.LeakCanary.core)
    implementation(Deps.LeakCanary.objectWatcher)

    testImplementation(Deps.Androidx.Test.core)
    testImplementation(Deps.junit)
    testImplementation(Deps.OkHttp3.mockWebServer)
    testImplementation(Deps.robolectric)
    testImplementation(Deps.mockk)
    testImplementation(Deps.truth)
}

scabbard {
    enabled = true
}
