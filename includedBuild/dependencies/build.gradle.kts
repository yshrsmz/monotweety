plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "net.yslibrary.monotweety.dependencies"
version = "SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(gradleKotlinDsl())
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
        create("dependencies") {
            id = "dependencies"
            implementationClass = "net.yslibrary.monotweety.dependencies.DependenciesPlugin"
        }
    }
}
