package net.yslibrary.monotweety.setting.domain

import io.reactivex.Observable
import net.yslibrary.monotweety.appdata.appinfo.AppInfo
import net.yslibrary.monotweety.appdata.appinfo.AppInfoManager
import net.yslibrary.monotweety.appdata.setting.SettingDataManager
import net.yslibrary.monotweety.base.di.AppScope
import javax.inject.Inject

@AppScope
class SelectedTimelineAppInfoManager @Inject constructor(
    private val appInfoManager: AppInfoManager,
    private val settingDataManager: SettingDataManager
) {

    fun get(): Observable<AppInfo> {
        return settingDataManager.timelineAppPackageName()
            .flatMap { appInfoManager.appInfo(it).toObservable() }
    }

    fun set(selectedApp: AppInfo) {
        settingDataManager.timelineAppPackageName(selectedApp.packageName)
    }
}
