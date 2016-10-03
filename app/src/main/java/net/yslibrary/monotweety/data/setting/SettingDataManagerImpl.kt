package net.yslibrary.monotweety.data.setting

import com.f2prateek.rx.preferences.RxSharedPreferences
import rx.Observable

/**
 * Created by yshrsmz on 2016/09/29.
 */
class SettingDataManagerImpl(private val prefs: RxSharedPreferences) : SettingDataManager {

  private val notificationEnabled = prefs.getBoolean(NOTIFICATION_ENABLED, false)

  private val startOnRebootEnabled = prefs.getBoolean(START_ON_REBOOT_ENABLED, false)

  private val alwaysKeepDialogOpened = prefs.getBoolean(ALWAYS_KEEP_DIALOG_OPENED, false)

  override fun notificationEnabled(): Observable<Boolean> {
    return notificationEnabled.asObservable()
  }

  override fun notificationEnabled(enabled: Boolean) {
    notificationEnabled.set(enabled)
  }

  override fun startOnRebootEnabled(): Observable<Boolean> {
    return startOnRebootEnabled.asObservable()
  }

  override fun startOnRebootEnabled(enabled: Boolean) {
    startOnRebootEnabled.set(enabled)
  }

  override fun alwaysKeepDialogOpened(): Observable<Boolean> {
    return alwaysKeepDialogOpened.asObservable()
  }

  override fun alwaysKeepDialogOpened(enabled: Boolean) {
    alwaysKeepDialogOpened.set(enabled)
  }

  companion object {
    const val NOTIFICATION_ENABLED = "notification_enabled"
    const val START_ON_REBOOT_ENABLED = "start_on_reboot_enabled"
    const val ALWAYS_KEEP_DIALOG_OPENED = "always_keep_dialog_opened"
  }
}