package net.yslibrary.monotweety.buildhelper

import org.gradle.api.Project
import java.util.Properties

private fun Project.loadProperties(path: String): Properties {
    val propertiesFile = rootProject.rootDir.resolve(path)
    if (!propertiesFile.exists()) {
        throw IllegalStateException("'$path' does not exist")
    }
    val props = Properties()
    props.load(propertiesFile.inputStream())
    return props
}

internal fun Project.loadSecrets(): Properties = loadProperties("secret.properties")

fun splitAlternately(source: String): Pair<String, String> {
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
    return result1 to result2
}
