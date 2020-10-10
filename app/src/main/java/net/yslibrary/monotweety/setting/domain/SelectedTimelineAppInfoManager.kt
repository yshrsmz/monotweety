package net.yslibrary.monotweety.setting.domain

import io.reactivex.Observable
import net.yslibrary.monotweety.appdata.appinfo.AppInfo
import net.yslibrary.monotweety.appdata.appinfo.AppInfoManager
import net.yslibrary.monotweety.appdata.setting.SettingDataManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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
