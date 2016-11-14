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
class GetSelectedTimelineAppState @Inject constructor(private val appInfoManager: AppInfoManager,
                                                      private val settingDataManager: SettingDataManager) {

  fun execute(): Observable<SelectedTimelineAppState> {
    return Observable.combineLatest(
        settingDataManager.timelineAppEnabled(),
        settingDataManager.timelineAppPackageName()
            .flatMap { appInfoManager.appInfo(it).toObservable() },
        { enabled, appInfo -> SelectedTimelineAppState(enabled, appInfo) }
    )
  }

  data class SelectedTimelineAppState(val enabled: Boolean,
                                      val selectedApp: AppInfo)
}