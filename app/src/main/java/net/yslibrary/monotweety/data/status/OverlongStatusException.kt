package net.yslibrary.monotweety.data.status

class OverlongStatusException(val status: String,
                              val length: Int) : Exception("Message is too long to tweet: $length")