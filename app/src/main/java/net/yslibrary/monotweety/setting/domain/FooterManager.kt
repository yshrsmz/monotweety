package net.yslibrary.monotweety.setting.domain

import net.yslibrary.monotweety.base.di.AppScope
import net.yslibrary.monotweety.data.setting.SettingDataManager
import rx.Observable
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/11/05.
 */
@AppScope
class FooterManager @Inject constructor(private val settingDataManager: SettingDataManager) {

  fun enabled(): Observable<Boolean> = settingDataManager.footerEnabled()
  fun enabled(enabled: Boolean) = settingDataManager.footerEnabled(enabled)

  fun text(): Observable<String> = settingDataManager.footerText()
  fun text(text: String) = settingDataManager.footerText(text)
}