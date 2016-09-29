package net.yslibrary.monotweety.setting.domain

import net.yslibrary.monotweety.base.di.AppScope
import net.yslibrary.monotweety.data.setting.SettingDataManager
import rx.Observable
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/09/29.
 */
@AppScope
class NotificationEnabledManager @Inject constructor(private val settingDataManager: SettingDataManager) {

  fun get(): Observable<Boolean> = settingDataManager.notificationEnabled()

  fun set(enabled: Boolean) = settingDataManager.notificationEnabled(enabled)
}