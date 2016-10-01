package net.yslibrary.monotweety.data.status

/**
 * Created by yshrsmz on 2016/10/01.
 */
class OverlongStatusException(val status: String,
                              val length: Int) : Exception("Message is too long to tweet: $length")