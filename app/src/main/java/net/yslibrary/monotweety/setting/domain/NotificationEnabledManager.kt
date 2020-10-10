package net.yslibrary.monotweety.setting.domain

import io.reactivex.Observable
import net.yslibrary.monotweety.appdata.setting.SettingDataManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationEnabledManager @Inject constructor(private val settingDataManager: SettingDataManager) {

    fun get(): Observable<Boolean> = settingDataManager.notificationEnabled()

    fun set(enabled: Boolean) = settingDataManager.notificationEnabled(enabled)
}
