package net.yslibrary.monotweety.setting.domain

import net.yslibrary.monotweety.base.di.AppScope
import net.yslibrary.monotweety.data.setting.SettingDataManager
import rx.Observable
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/10/03.
 */
@AppScope
class AlwaysKeepDialogOpenedManager @Inject constructor(private val settingDataManager: SettingDataManager) {

  fun get(): Observable<Boolean> = settingDataManager.alwaysKeepDialogOpened()

  fun set(enabled: Boolean) = settingDataManager.alwaysKeepDialogOpened(enabled)
}