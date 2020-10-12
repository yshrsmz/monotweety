// Top-level build file where you can add configuration options common to all sub-projects/modules.
// `./gradlew build -Dscan` to check & upload result

plugins {
    id("dependencies")
    id("build-helper")
    id(Plugins.Ids.versions) version Versions.versions
    id(Plugins.Ids.androidApp) version Versions.agp apply false
    kotlin("android") version Versions.kotlin apply false
    id(Plugins.Ids.crashlytics) version Versions.crashlytics apply false
    id(Plugins.Ids.googleservices) version Versions.googleservices apply false
    id(Plugins.Ids.navSafeArgs) version Versions.navigation apply false
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}

tasks.wrapper {
    gradleVersion = Versions.gradle
    distributionType = Wrapper.DistributionType.ALL
}
