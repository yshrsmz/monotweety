package net.yslibrary.monotweety.base

/**
 * Created by yshrsmz on 2016/10/01.
 */
class ClockImpl : Clock {
  override fun currentTimeMillis(): Long {
    return System.currentTimeMillis()
  }
}