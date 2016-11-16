package net.yslibrary.monotweety.setting.domain

import net.yslibrary.monotweety.base.di.AppScope
import net.yslibrary.monotweety.data.appinfo.AppInfo
import net.yslibrary.monotweety.data.appinfo.AppInfoManager
import net.yslibrary.monotweety.data.setting.SettingDataManager
import rx.Observable
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/11/14.
 */
@AppScope
class SelectedTimelineAppInfoManager @Inject constructor(private val appInfoManager: AppInfoManager,
                                                         private val settingDataManager: SettingDataManager) {

  fun get(): Observable<AppInfo> {
    return settingDataManager.timelineAppPackageName()
        .flatMap { appInfoManager.appInfo(it).toObservable() }
  }

  fun set(selectedApp: AppInfo) {
    settingDataManager.timelineAppPackageName(selectedApp.packageName)
  }
}