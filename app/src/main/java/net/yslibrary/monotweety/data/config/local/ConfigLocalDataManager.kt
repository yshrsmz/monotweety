package net.yslibrary.monotweety.data.config.local

import io.reactivex.Observable

interface ConfigLocalDataManager {

  fun shortUrlLengthHttps(): Observable<Int>
  fun shortUrlLengthHttps(length: Int)

  fun updatedAt(): Long
  fun updatedAt(timestamp: Long)

  fun outdated(): Boolean
}