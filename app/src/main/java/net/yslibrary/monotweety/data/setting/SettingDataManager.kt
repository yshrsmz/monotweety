package net.yslibrary.monotweety.data.setting

import rx.Completable
import rx.Observable

/**
 * Created by yshrsmz on 2016/09/29.
 */
interface SettingDataManager {
  fun notificationEnabled(): Observable<Boolean>
  fun notificationEnabled(enabled: Boolean)

  fun startOnRebootEnabled(): Observable<Boolean>
  fun startOnRebootEnabled(enabled: Boolean)

  fun keepOpen(): Observable<Boolean>
  fun keepOpen(enabled: Boolean)

  fun footerEnabled(): Observable<Boolean>
  fun footerEnabled(enabled: Boolean)

  fun footerText(): Observable<String>
  fun footerText(text: String)

  fun clear(): Completable
}