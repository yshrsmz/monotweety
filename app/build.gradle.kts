//apply plugin: "com.android.application"
//apply plugin: "kotlin-android"
//apply plugin: "kotlin-kapt"
////apply plugin: "com.getkeepsafe.dexcount"
//apply plugin: "com.google.gms.google-services"
//apply plugin: "com.google.firebase.crashlytics"
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
//    id("com.getkeepsafe.dexcount")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("scabbard.gradle") version "0.4.0"
}

apply(from = rootProject.file("gradle/jacoco.gradle"))

fun getSecretProperty(name: String): String {
    val properties = Properties()
    properties.load(project.rootProject.file("secret.properties").inputStream())

    return properties.getProperty(name.toString())
}

fun splitAlternately(source: String): Array<String> {
    var result1 = ""
    var result2 = ""

    var i = 0
    val len = source.length
    while (i < len) {
        if (i % 2 == 0) {
            // even
            result1 += source[i]
        } else {
            result2 += source[i]
        }
        i++
    }
    return arrayOf(result1, result2)
}

android {
    compileSdkVersion(Versions.compileSdk)

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

    val twitterApiKeys = splitAlternately(getSecretProperty("twitter.api.key"))
    val twitterApiSecrets = splitAlternately(getSecretProperty("twitter.api.secret"))
    defaultConfig {
        applicationId = "net.yslibrary.monotweety"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = 58
        versionName = "1.9.0"

        buildConfigField("String", "TWITTER_API_KEY_1", "\"${twitterApiKeys[0]}\"")
        buildConfigField("String", "TWITTER_API_KEY_2", "\"${twitterApiKeys[1]}\"")
        buildConfigField("String", "TWITTER_API_SECRET_1", "\"${twitterApiSecrets[0]}\"")
        buildConfigField("String", "TWITTER_API_SECRET_2", "\"${twitterApiSecrets[1]}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    dexOptions {
        jumboMode = true
    }

    lintOptions {
        lintConfig = rootProject.file("gradle/lint.xml")
        isAbortOnError = false
        textReport = true
        textOutput("stdout")
//    xmlReport true
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
    javacOptions {
        option("-Adagger.fastInit=enabled")
    }
}

dependencies {
    val retrofit_version = "2.9.0"
    val conductor_version = "3.0.0-rc6"
    val storio_version = "3.0.0"

    implementation(Deps.Kotlin.Stdlib.jdk8)

    implementation(Deps.Androidx.appcompat)
    implementation(Deps.Androidx.recyclerview)
    implementation(Deps.Androidx.constraintlayout)
    implementation(Deps.Androidx.multidex)
    implementation(Deps.Androidx.ktx)
    implementation(Deps.Androidx.fragmentKtx)
    implementation(Deps.Androidx.Lifecycle.runtime)
    implementation(Deps.Androidx.Lifecycle.commonJava8)
    implementation(Deps.Androidx.Navigation.fragment)
    implementation(Deps.Androidx.Navigation.ui)
    implementation(Deps.Androidx.viewmodel)

    implementation(Deps.material)

    implementation(platform(Deps.Firebase.bom))
    implementation(Deps.Firebase.analytics)
    implementation(Deps.Firebase.crashlytics)

    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")

    implementation("com.twitter.twittertext:twitter-text:3.1.0")
    implementation("com.twitter.sdk.android:twitter-core:3.3.0")

    implementation(Deps.OkHttp3.core)

    implementation(Deps.Dagger.core)
    kapt(Deps.Dagger.compiler)

    implementation("com.pushtorefresh.storio3:sqlite:$storio_version")
    implementation(Deps.RxJava2.core)
    implementation(Deps.RxJava2.android)
    implementation(Deps.RxJava2.kotlin)

    implementation("com.gojuno.koptional:koptional:1.7.0")
    implementation("com.gojuno.koptional:koptional-rxjava2-extensions:1.7.0")

    implementation(Deps.RxJava2.proguard)

    implementation(Deps.RxBinding.core)
    implementation(Deps.RxBinding.appcompat)
    implementation(Deps.RxBinding.recyclerview)

    implementation("com.jakewharton.rxrelay2:rxrelay:2.1.1")

    implementation("com.f2prateek.rx.preferences2:rx-preferences:2.0.1")

    implementation("com.bluelinelabs:conductor:${conductor_version}")
    implementation("com.bluelinelabs:conductor-androidx-transition:${conductor_version}")
    implementation("com.bluelinelabs:conductor-rxlifecycle2:${conductor_version}")

    implementation(Deps.adapterDelegates)

    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation(Deps.Glide.core)
    kapt(Deps.Glide.compiler)

    implementation(Deps.timber)
    implementation(Deps.threetenabp)

    implementation(Deps.licenseAdapter)

    implementation("com.github.gabrielemariotti.changeloglib:changelog:2.1.0")

    implementation("me.drakeet.support:toastcompat:1.1.0")

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