package net.yslibrary.monotweety.appdata.status

class OverlongStatusException(
    val status: String,
    val length: Int
) : Exception("Message is too long to tweet: $length")
