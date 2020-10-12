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

//    implementation("net.yslibrary.monotweety.dependencies:dependencies:SNAPSHOT")

    implementation(Plugins.kotlin)
    implementation(Plugins.android)
}

kotlin {
    target {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    // TODO: recheck when Android Studio 4.1 is available
    // Same as 'implementation("net.yslibrary.monotweety.dependencies:dependencies:SNAPSHOT")', but will make autocompletion work
    sourceSets.getByName("main").kotlin.srcDir("../dependencies/src/main/java")
}

gradlePlugin {
    plugins {
        create("build-helper") {
            id = "build-helper"
            implementationClass = "net.yslibrary.monotweety.buildhelper.BuildHelperPlugin"
        }
    }
}
