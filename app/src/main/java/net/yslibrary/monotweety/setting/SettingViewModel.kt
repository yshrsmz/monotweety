package net.yslibrary.monotweety.setting

import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import net.yslibrary.monotweety.Config
import net.yslibrary.monotweety.data.appinfo.AppInfo
import net.yslibrary.monotweety.data.user.User
import net.yslibrary.monotweety.setting.domain.*
import net.yslibrary.monotweety.user.domain.GetUser
import timber.log.Timber

class SettingViewModel(private val config: Config,
                       private val notificationEnabledManager: NotificationEnabledManager,
                       private val getUser: GetUser,
                       private val keepOpenManager: KeepOpenManager,
                       private val footerStateManager: FooterStateManager,
                       private val getInstalledSupportedApps: GetInstalledSupportedApps,
                       private val selectedTimelineAppInfoManager: SelectedTimelineAppInfoManager) {

    private val userSubject = BehaviorSubject.createDefault<Optional<User>>(None)

    private val logoutRequestsSubject = PublishSubject.create<Unit>()

    private val privacyPolicyRequestsSubject = PublishSubject.create<String>()

    private val licenseRequestsSubject = PublishSubject.create<Unit>()

    private val developerRequestsSubject = PublishSubject.create<String>()

    private val shareRequestsSubject = PublishSubject.create<String>()

    private val googlePlayRequestsSubject = PublishSubject.create<String>()

    private val openProfileRequestsSubject = PublishSubject.create<String>()

    private val changelogRequestsSubject = PublishSubject.create<Unit>()

    private val githubRequestsSubject = PublishSubject.create<String>()

    val notificationEnabledChanged: Observable<Boolean>
        get() = notificationEnabledManager.get()

    val keepOpen: Observable<Boolean>
        get() = keepOpenManager.get()

    val user: Observable<Optional<User>>
        get() = userSubject

    val openProfileRequests: Observable<String>
        get() = openProfileRequestsSubject

    val logoutRequests: Observable<Unit>
        get() = logoutRequestsSubject

    val privacyPolicyRequests: Observable<String>
        get() = privacyPolicyRequestsSubject

    val licenseRequests: Observable<Unit>
        get() = licenseRequestsSubject

    val developerRequests: Observable<String>
        get() = developerRequestsSubject

    val shareRequests: Observable<String>
        get() = shareRequestsSubject

    val googlePlayRequests: Observable<String>
        get() = googlePlayRequestsSubject

    val changelogRequests: Observable<Unit>
        get() = changelogRequestsSubject

    val githubRequests: Observable<String>
        get() = githubRequestsSubject

    val footerState: Observable<FooterStateManager.State>
        get() = footerStateManager.get()

    val installedSupportedApps: Single<List<AppInfo>>
        get() = getInstalledSupportedApps.execute()

    val selectedTimelineApp: Observable<AppInfo>
        get() = selectedTimelineAppInfoManager.get()

    init {
        getUser.execute()
            .subscribe({ userSubject.onNext(it) },
                { Timber.e(it, it.message) })
    }

    fun onNotificationEnabledChanged(enabled: Boolean) {
        notificationEnabledManager.set(enabled)
    }

    fun onKeepOpenChanged(enabled: Boolean) {
        keepOpenManager.set(enabled)
    }

    fun onFooterStateChanged(enabled: Boolean, footerText: String) {
        footerStateManager.set(FooterStateManager.State(enabled, footerText))
    }

    fun onTimelineAppChanged(selectedApp: AppInfo) {
        selectedTimelineAppInfoManager.set(selectedApp)
    }

    fun onOpenProfileRequested() {
        userSubject.value?.toNullable()?.let { openProfileRequestsSubject.onNext(it.screenName) }
    }

    fun onLogoutRequested() {
        logoutRequestsSubject.onNext(Unit)
    }

    fun onLicenseRequested() {
        licenseRequestsSubject.onNext(Unit)
    }

    fun onDeveloperRequested() {
        developerRequestsSubject.onNext(config.developerUrl)
    }

    fun onShareRequested() {
        shareRequestsSubject.onNext(config.googlePlayUrl)
    }

    fun onGooglePlayRequested() {
        googlePlayRequestsSubject.onNext(config.googlePlayUrl)
    }

    fun onChangelogRequested() {
        changelogRequestsSubject.onNext(Unit)
    }

    fun onGitHubRequested() {
        githubRequestsSubject.onNext(config.githubUrl)
    }

    fun onPrivacyPolicyRequested() {
        privacyPolicyRequestsSubject.onNext(config.privacyPolicyUrl)
    }

    data class TimelineAppInfo(val enabled: Boolean,
                               val supportedApps: List<AppInfo>,
                               val selectedApp: AppInfo)
}
