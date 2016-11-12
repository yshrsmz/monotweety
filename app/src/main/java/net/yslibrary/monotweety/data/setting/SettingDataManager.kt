package net.yslibrary.monotweety.data.setting

import net.yslibrary.monotweety.data.AppInfo
import rx.Completable
import rx.Observable
import rx.Single

/**
 * Created by yshrsmz on 2016/09/29.
 */
interface SettingDataManager {
  fun notificationEnabled(): Observable<Boolean>
  fun notificationEnabled(enabled: Boolean)

  fun keepOpen(): Observable<Boolean>
  fun keepOpen(enabled: Boolean)

  fun footerEnabled(): Observable<Boolean>
  fun footerEnabled(enabled: Boolean)

  fun footerText(): Observable<String>
  fun footerText(text: String)

  fun installedApps(): Single<List<AppInfo>>
  fun selectedApp(): Observable<AppInfo?>
  fun selectedApp(appInfo: AppInfo)

  fun clear(): Completable
}