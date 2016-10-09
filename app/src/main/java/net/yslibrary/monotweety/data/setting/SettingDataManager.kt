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

  fun keepDialogOpen(): Observable<Boolean>
  fun keepDialogOpen(enabled: Boolean)

  fun clear(): Completable
}