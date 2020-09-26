package net.yslibrary.monotweety.util

/**
 * concat provided strings alternately
 *
 * @param firstSource even index of the result string will be from this string.
 * @param secondSource uneven index of result string will be from this string.
 * @return String
 */
fun concatAlternately(firstSource: String, secondSource: String): String {
    var firstSource1 = firstSource
    var secondSource1 = secondSource
    var concat = ""
    var i = 0
    val len = firstSource1.length + secondSource1.length
    while (i < len) {
        var target: String
        if (i % 2 == 0) {
            // even
            target = firstSource1.substring(0, 1)
            firstSource1 = firstSource1.substring(1)

            if (firstSource1.length == 0) {
                target += secondSource1
                i = len
            }
        } else {
            target = secondSource1.substring(0, 1)
            secondSource1 = secondSource1.substring(1)

            if (secondSource1.isEmpty()) {
                target += firstSource1
                i = len
            }
        }
        concat += target
        i++
    }

    return concat
}