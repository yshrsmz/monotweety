package net.yslibrary.monotweety.data.setting

import rx.Completable
import rx.Observable

interface SettingDataManager {
  fun notificationEnabled(): Observable<Boolean>
  fun notificationEnabled(enabled: Boolean)

  fun keepOpen(): Observable<Boolean>
  fun keepOpen(enabled: Boolean)

  fun footerEnabled(): Observable<Boolean>
  fun footerEnabled(enabled: Boolean)

  fun footerText(): Observable<String>
  fun footerText(text: String)


  fun timelineAppEnabled(): Observable<Boolean>
  fun timelineAppEnabled(enabled: Boolean)

  fun timelineAppPackageName(): Observable<String>
  fun timelineAppPackageName(packageName: String)

  fun clear(): Completable
}