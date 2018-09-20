package net.yslibrary.monotweety.data.setting

import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Implementation of SettingDataManager.
 * This class is open just for testing.
 */
open class SettingDataManagerImpl(private val prefs: RxSharedPreferences) : SettingDataManager {

    private val notificationEnabled = prefs.getBoolean(NOTIFICATION_ENABLED, false)

    private val footerEnabled = prefs.getBoolean(FOOTER_ENABLED, false)
    private val footerText = prefs.getString(FOOTER_TEXT, "")

    private val timelineAppEnabled = prefs.getBoolean(TIMELINE_APP_ENABLED, false)
    private val timelineAppPackageName = prefs.getString(TIMELINE_APP_PACKAGE_NAME, "")

    override fun notificationEnabled(): Observable<Boolean> {
        return notificationEnabled.asObservable()
    }

    override fun notificationEnabled(enabled: Boolean) {
        notificationEnabled.set(enabled)
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

    override fun timelineAppEnabled(): Observable<Boolean> {
        return timelineAppEnabled.asObservable()
    }

    override fun timelineAppEnabled(enabled: Boolean) {
        timelineAppEnabled.set(enabled)
    }

    override fun timelineAppPackageName(): Observable<String> {
        return timelineAppPackageName.asObservable()
    }

    override fun timelineAppPackageName(packageName: String) {
        timelineAppPackageName.set(packageName)
    }

    override fun clear(): Completable {
        return Completable.fromAction {
            notificationEnabled.delete()
            footerEnabled.delete()
            footerText.delete()
            timelineAppPackageName.delete()
        }
    }

    companion object {
        const val NOTIFICATION_ENABLED = "notification_enabled"

        const val FOOTER_ENABLED = "footer_enabled"
        const val FOOTER_TEXT = "footer_text"

        const val TIMELINE_APP_ENABLED = "timeline_app_enabled"
        const val TIMELINE_APP_PACKAGE_NAME = "timeline_app_package_name"
    }
}
