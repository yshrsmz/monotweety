package net.yslibrary.monotweety.setting

import net.yslibrary.monotweety.Config
import net.yslibrary.monotweety.data.user.User
import net.yslibrary.monotweety.setting.domain.KeepDialogOpenManager
import net.yslibrary.monotweety.setting.domain.NotificationEnabledManager
import net.yslibrary.monotweety.user.domain.GetUser
import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject
import timber.log.Timber


/**
 * Created by yshrsmz on 2016/09/29.
 */

class SettingViewModel(private val config: Config,
                       private val notificationEnabledManager: NotificationEnabledManager,
                       private val getUser: GetUser,
                       private val keepDialogOpenManager: KeepDialogOpenManager) {

  private val userSubject = BehaviorSubject<User?>(null)

  private val logoutRequestsSubject = PublishSubject<Unit>()

  private val licenseRequestsSubject = PublishSubject<Unit>()

  private val developerRequestsSubject = PublishSubject<String>()

  private val googlePlayRequestsSubject = PublishSubject<String>()

  private val openProfileRequestsSubject = PublishSubject<String>()

  private val changelogRequestsSubject = PublishSubject<Unit>()

  private val githubRequestsSubject = PublishSubject<String>()

  val notificationEnabledChanged: Observable<Boolean>
    get() = notificationEnabledManager.get()

  val keepDialogOpen: Observable<Boolean>
    get() = keepDialogOpenManager.get()

  val user: Observable<User?>
    get() = userSubject.asObservable()

  val openProfileRequests: Observable<String>
    get() = openProfileRequestsSubject.asObservable()

  val logoutRequests: Observable<Unit>
    get() = logoutRequestsSubject.asObservable()

  val licenseRequests: Observable<Unit>
    get() = licenseRequestsSubject.asObservable()

  val developerRequests: Observable<String>
    get() = developerRequestsSubject.asObservable()

  val googlePlayRequests: Observable<String>
    get() = googlePlayRequestsSubject.asObservable()

  val changelogRequests: Observable<Unit>
    get() = changelogRequestsSubject.asObservable()

  val githubRequests: Observable<String>
    get() = githubRequestsSubject.asObservable()

  init {
    getUser.execute()
        .subscribe({ userSubject.onNext(it) },
            { Timber.e(it, it.message) })
  }

  fun onNotificationEnabledChanged(enabled: Boolean) {
    notificationEnabledManager.set(enabled)
  }

  fun onKeepDialogOpenChanged(enabled: Boolean) {
    keepDialogOpenManager.set(enabled)
  }

  fun onOpenProfileRequested() {
    userSubject.value?.let {
      openProfileRequestsSubject.onNext(it.screenName)
    }
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

  fun onGooglePlayRequested() {
    googlePlayRequestsSubject.onNext(config.googlePlayUrl)
  }

  fun onChangelogRequested() {
    changelogRequestsSubject.onNext(Unit)
  }

  fun onGithubRequested() {
    githubRequestsSubject.onNext(config.githubUrl)
  }
}
