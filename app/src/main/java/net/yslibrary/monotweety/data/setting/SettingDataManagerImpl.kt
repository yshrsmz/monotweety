package net.yslibrary.monotweety.data.setting

import android.content.Intent
import android.content.pm.PackageManager
import com.f2prateek.rx.preferences.RxSharedPreferences
import net.yslibrary.monotweety.base.toSingle
import net.yslibrary.monotweety.data.AppInfo
import rx.Completable
import rx.Observable
import rx.Single

/**
 * Implementation of SettingDataManager.
 * This class is open just for testing.
 */
open class SettingDataManagerImpl(private val packageManager: PackageManager,
                                  private val prefs: RxSharedPreferences) : SettingDataManager {

  private val notificationEnabled = prefs.getBoolean(NOTIFICATION_ENABLED, false)

  private val keepOpen = prefs.getBoolean(KEEP_OPEN, false)

  private val footerEnabled = prefs.getBoolean(FOOTER_ENABLED, false)
  private val footerText = prefs.getString(FOOTER_TEXT, "")

  private val selectedApp = prefs.getString(SELECTED_APP, "")

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

  override fun installedApps(): Single<List<AppInfo>> {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)

    return packageManager.queryIntentActivities(intent, 0)
        .map { AppInfo(name = it.activityInfo.name, packageName = it.activityInfo.packageName) }
        .toSingle()
  }

  override fun selectedApp(): Observable<AppInfo?> {
    return selectedApp.asObservable()
        .map {
          if (isAppInstalled(it)) {
            val packageInfo = packageManager.getPackageInfo(it, PackageManager.GET_ACTIVITIES)
            AppInfo(name = packageInfo.applicationInfo.name, packageName = it)
          } else {
            null
          }
        }
  }

  override fun selectedApp(appInfo: AppInfo) {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  fun isAppInstalled(packageName: String): Boolean {
    try {
      packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
      return true
    } catch (e: PackageManager.NameNotFoundException) {
      return false
    }
  }

  override fun clear(): Completable {
    return Completable.fromAction {
      notificationEnabled.delete()
      keepOpen.delete()
      footerEnabled.delete()
      footerText.delete()
    }
  }

  companion object {
    const val NOTIFICATION_ENABLED = "notification_enabled"
    const val KEEP_OPEN = "keep_open"

    const val FOOTER_ENABLED = "footer_enabled"
    const val FOOTER_TEXT = "footer_text"

    const val SELECTED_APP = "selected_app"
  }
}