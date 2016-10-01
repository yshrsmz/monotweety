package net.yslibrary.monotweety.data.config

import rx.Observable

/**
 * Created by yshrsmz on 2016/10/01.
 */
interface ConfigDataManager {
  fun shortUrlLengthHttps(): Observable<Int>
}