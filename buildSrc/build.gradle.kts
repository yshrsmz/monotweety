plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    jcenter()
}

// TODO: recheck when Android Studio 4.2 is available
// Same as 'implementation("net.yslibrary.monotweety.dependencies:dependencies:SNAPSHOT")', but will make autocompletion work
kotlin.sourceSets.getByName("main").kotlin.srcDir("../includedBuild/dependencies/src/main/java")

dependencies {
//    api("net.yslibrary.monotweety.dependencies:dependencies:SNAPSHOT")
}
