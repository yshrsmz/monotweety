package net.yslibrary.monotweety.data.config.local

import rx.Observable

/**
 * Created by yshrsmz on 2016/10/01.
 */
interface ConfigLocalDataManager {

  fun shortUrlLengthHttps(): Observable<Int>
  fun shortUrlLengthHttps(length: Int)

  fun updatedAt(): Long
  fun updatedAt(timestamp: Long)

  fun outdated(): Boolean
}