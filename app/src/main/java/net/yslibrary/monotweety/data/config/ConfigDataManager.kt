package net.yslibrary.monotweety.data.config

import rx.Observable

interface ConfigDataManager {
  fun shortUrlLengthHttps(): Observable<Int>
}