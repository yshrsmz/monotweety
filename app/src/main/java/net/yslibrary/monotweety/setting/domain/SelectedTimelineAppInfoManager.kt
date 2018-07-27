package net.yslibrary.monotweety.setting.domain

import io.reactivex.Observable
import net.yslibrary.monotweety.base.di.AppScope
import net.yslibrary.monotweety.data.appinfo.AppInfo
import net.yslibrary.monotweety.data.appinfo.AppInfoManager
import net.yslibrary.monotweety.data.setting.SettingDataManager
import javax.inject.Inject

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
