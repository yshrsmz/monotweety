package net.yslibrary.monotweety.data.config

import io.reactivex.Observable

interface ConfigDataManager {
  fun shortUrlLengthHttps(): Observable<Int>
}