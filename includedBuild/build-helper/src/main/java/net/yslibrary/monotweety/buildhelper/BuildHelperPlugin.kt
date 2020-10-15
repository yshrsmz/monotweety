package net.yslibrary.monotweety.buildhelper

import Deps
import Plugins
import Versions
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withConvention
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class BuildHelperPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.allprojects {
            configureRepositories()

            pluginManager.withPlugin(Plugins.Ids.androidApp) {
                configureAndroidApp()
            }
            pluginManager.withPlugin(Plugins.Ids.androidLib) {
                configureAndroidLib()
            }
            pluginManager.withPlugin("org.jetbrains.kotlin.kapt") {
                configureKapt()
            }
            pluginManager.withPlugin("java-library") {
                println("jvm: ${path}")
            }
            pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
                println("kotlin-jvm: $path")
            }
        }
    }
}

private fun Project.configureRepositories() {
    repositories {
        mavenCentral()
        google()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx/")
        maven(url = "https://dl.bintray.com/yshrsmz/twitter4kt/")
    }
}

private fun Project.configureAndroidCommon() {
    val extension =
        extensions.findByType(com.android.build.gradle.BaseExtension::class.java) ?: return

    extension.apply {
        compileSdkVersion(Versions.compileSdk)
        defaultConfig {
            minSdkVersion(Versions.minSdk)
            targetSdkVersion(Versions.targetSdk)

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        lintOptions {
            lintConfig = rootProject.file("gradle/lint.xml")
            isAbortOnError = false
            textReport = true
            textOutput("stdout")
        }

        testOptions {
            unitTests.isIncludeAndroidResources = true
        }
    }

    configureKotlinJvm()
}

private fun Project.configureAndroidApp() {
    configureAndroidCommon()
    val extension =
        extensions.findByType(BaseAppModuleExtension::class.java)
            ?: return

    val secrets = loadSecrets()
    val twitterApiKeys = splitAlternately(secrets.getProperty("twitter.api.key"))
    val twitterApiSecrets = splitAlternately(secrets.getProperty("twitter.api.secret"))

    extension.apply {
        defaultConfig {
            buildConfigField("String", "TWITTER_API_KEY_1", "\"${twitterApiKeys.first}\"")
            buildConfigField("String", "TWITTER_API_KEY_2", "\"${twitterApiKeys.second}\"")
            buildConfigField("String", "TWITTER_API_SECRET_1", "\"${twitterApiSecrets.first}\"")
            buildConfigField("String", "TWITTER_API_SECRET_2", "\"${twitterApiSecrets.second}\"")
        }
        dexOptions {
            jumboMode = true
        }

        @Suppress("UnstableApiUsage")
        compileOptions {
            isCoreLibraryDesugaringEnabled = true
        }

        @Suppress("UnstableApiUsage")
        buildFeatures {
            viewBinding = true
        }
    }

    dependencies.apply {
        add("coreLibraryDesugaring", Deps.desugarJdk)
    }
}

private fun Project.configureAndroidLib() {
    configureAndroidCommon()

    val extension =
        extensions.findByType(LibraryExtension::class.java) ?: return

    extension.apply {
        defaultConfig {
            consumerProguardFiles("consumer-rules.pro")
        }
        buildTypes {
            getByName("release") {
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
        @Suppress("UnstableApiUsage")
        buildFeatures {
            buildConfig = false
        }
    }
}

private fun Project.configureKotlinJvm() {
    tasks.withType<KotlinCompile>().all {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
        kotlinOptions.jvmTarget = "1.8"
    }

    project.withConvention(JavaPluginConvention::class) {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

private fun Project.configureKapt() {
    val extension = extensions.findByType(KaptExtension::class.java) ?: return
    extension.apply {
        correctErrorTypes = true
        useBuildCache = true
        javacOptions {
            option("-Adagger.fastInit=enabled")
        }
    }
}
