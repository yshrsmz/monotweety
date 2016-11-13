package net.yslibrary.monotweety.data.setting

import com.f2prateek.rx.preferences.RxSharedPreferences
import rx.Completable
import rx.Observable

/**
 * Implementation of SettingDataManager.
 * This class is open just for testing.
 */
open class SettingDataManagerImpl(private val prefs: RxSharedPreferences) : SettingDataManager {

  private val notificationEnabled = prefs.getBoolean(NOTIFICATION_ENABLED, false)

  private val keepOpen = prefs.getBoolean(KEEP_OPEN, false)

  private val footerEnabled = prefs.getBoolean(FOOTER_ENABLED, false)
  private val footerText = prefs.getString(FOOTER_TEXT, "")

  private val selectedPackageName = prefs.getString(SELECTED_PACKAGE_NAME, "")

  override fun notificationEnabled(): Observable<Boolean> {
    return notificationEnabled.asObservable()
  }

  override fun notificationEnabled(enabled: Boolean) {
    notificationEnabled.set(enabled)
  }

  override fun keepOpen(): Observable<Boolean> {
    return keepOpen.asObservable()
  }

  override fun keepOpen(enabled: Boolean) {
    keepOpen.set(enabled)
  }

  override fun footerEnabled(): Observable<Boolean> {
    return footerEnabled.asObservable()
  }

  override fun footerEnabled(enabled: Boolean) {
    footerEnabled.set(enabled)
  }

  override fun footerText(): Observable<String> {
    return footerText.asObservable()
  }

  override fun footerText(text: String) {
    footerText.set(text)
  }

  override fun selectedPackageName(): Observable<String> {
    return selectedPackageName.asObservable()
  }

  override fun selectedPackageName(packageName: String) {
    selectedPackageName.set(packageName)
  }

  override fun clear(): Completable {
    return Completable.fromAction {
      notificationEnabled.delete()
      keepOpen.delete()
      footerEnabled.delete()
      footerText.delete()
      selectedPackageName.delete()
    }
  }

  companion object {
    const val NOTIFICATION_ENABLED = "notification_enabled"
    const val KEEP_OPEN = "keep_open"

    const val FOOTER_ENABLED = "footer_enabled"
    const val FOOTER_TEXT = "footer_text"

    const val SELECTED_PACKAGE_NAME = "selected_package_name"
  }
}