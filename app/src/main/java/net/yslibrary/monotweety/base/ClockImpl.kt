package net.yslibrary.monotweety.base

class ClockImpl : Clock {
    override fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}
