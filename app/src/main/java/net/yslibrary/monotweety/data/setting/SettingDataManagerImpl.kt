package net.yslibrary.monotweety.data.setting

import com.f2prateek.rx.preferences.RxSharedPreferences
import rx.Observable

/**
 * Created by yshrsmz on 2016/09/29.
 */
class SettingDataManagerImpl(private val prefs: RxSharedPreferences) : SettingDataManager {

  private val notificationEnabled = prefs.getBoolean(NOTIFICATION_ENABLED, false)

  private val startOnRebootEnabled = prefs.getBoolean(START_ON_REBOOT_ENABLED, false)

  private val keepDialogOpened = prefs.getBoolean(KEEP_DIALOG_OPEN, false)

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

  override fun keepDialogOpened(): Observable<Boolean> {
    return keepDialogOpened.asObservable()
  }

  override fun keepDialogOpened(enabled: Boolean) {
    keepDialogOpened.set(enabled)
  }

  companion object {
    const val NOTIFICATION_ENABLED = "notification_enabled"
    const val START_ON_REBOOT_ENABLED = "start_on_reboot_enabled"
    const val KEEP_DIALOG_OPEN = "keep_dialog_open"
  }
}