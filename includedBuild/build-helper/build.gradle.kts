plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("dependencies")
}

group = "net.yslibrary.monotweety.buildhelper"
version = "SNAPSHOT"

repositories {
    mavenCentral()
    google()
    jcenter()
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(gradleKotlinDsl())

    implementation("net.yslibrary.monotweety.dependencies:dependencies:SNAPSHOT")

    implementation(Plugins.kotlin)
    implementation(Plugins.android)
}

java {
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
    targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
}

kotlin {
    target {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
}

gradlePlugin {
    plugins {
        create("build-helper") {
            id = "build-helper"
            implementationClass = "net.yslibrary.monotweety.buildhelper.BuildHelperPlugin"
        }
    }
}
