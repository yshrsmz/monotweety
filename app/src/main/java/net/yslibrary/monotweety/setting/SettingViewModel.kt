package net.yslibrary.monotweety.setting

import net.yslibrary.monotweety.setting.domain.NotificationEnabledManager
import rx.Observable


/**
 * Created by yshrsmz on 2016/09/29.
 */

class SettingViewModel(private val notificationEnabledManager: NotificationEnabledManager) {

  val notificationEnabledChanged: Observable<Boolean>
    get() = notificationEnabledManager.get()


  fun onNotificationEnabledChanged(enabled: Boolean) {
    notificationEnabledManager.set(enabled)
  }
}
