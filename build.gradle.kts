// Top-level build file where you can add configuration options common to all sub-projects/modules.
// `./gradlew build -Dscan` to check & upload result

plugins {
    id("com.github.ben-manes.versions") version "0.33.0"
    id("com.android.application") version "4.0.1" apply false
    kotlin("android") version "1.4.10" apply false
    id("com.google.firebase.crashlytics") version "2.3.0" apply false
    id("com.google.gms.google-services") version "4.3.3" apply false
    id("androidx.navigation.safeargs.kotlin") version Versions.navigation apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
//        maven { url 'https://dl.bintray.com/kotlin/kotlin-eap' }
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}
buildscript {
    val kotlin_version by extra("1.4.10")
    dependencies {
        "classpath"("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

tasks.wrapper {
    gradleVersion = "6.6.1"
    distributionType = Wrapper.DistributionType.ALL
}
