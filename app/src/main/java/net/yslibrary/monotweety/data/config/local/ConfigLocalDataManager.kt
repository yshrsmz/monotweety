package net.yslibrary.monotweety.data.config.local

import rx.Observable

interface ConfigLocalDataManager {

  fun shortUrlLengthHttps(): Observable<Int>
  fun shortUrlLengthHttps(length: Int)

  fun updatedAt(): Long
  fun updatedAt(timestamp: Long)

  fun outdated(): Boolean
}