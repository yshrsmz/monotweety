import com.google.protobuf.gradle.builtins
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.google.protobuf") version "0.8.13"
}

android {
    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.4"
    }
}

dependencies {
    implementation(project(Deps.Modules.diCommon))
    implementation(Deps.Kotlin.Stdlib.jdk8)
    api(Deps.Kotlin.Coroutines.core)
    implementation(Deps.Kotlin.Coroutines.android)
    api(Deps.Kotlin.datetime)

    api(Deps.Twitter4kt.core)
    implementation(Deps.Twitter4kt.v1)

    implementation(Deps.Androidx.ktx)
    implementation(Deps.Androidx.appcompat)
    api(Deps.Androidx.DataStore.core)
    implementation(Deps.Protobuf.core)

    implementation(Deps.Dagger.core)
    kapt(Deps.Dagger.compiler)

    implementation(Deps.timber)

    testImplementation(Deps.junit)
}

protobuf {
    protoc {
        artifact = Deps.Protobuf.protoc
    }
    // Generates the java Protobuf-lite code for the Protobufs in this project. See
    // https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
    // for more information.
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}
