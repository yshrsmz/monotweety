package net.yslibrary.monotweety.setting

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

class SettingViewModel(private val notificationEnabledManager: NotificationEnabledManager,
                       private val getUser: GetUser,
                       private val keepDialogOpenManager: KeepDialogOpenManager) {

  private val userSubject = BehaviorSubject<User?>(null)

  private val logoutRequestsSubject = PublishSubject<Unit>()

  private val licenseRequestsSubject = PublishSubject<Unit>()

  val notificationEnabledChanged: Observable<Boolean>
    get() = notificationEnabledManager.get()

  val keepDialogOpen: Observable<Boolean>
    get() = keepDialogOpenManager.get()

  val logoutRequests: Observable<Unit>
    get() = logoutRequestsSubject.asObservable()

  val licenseRequests: Observable<Unit>
    get() = licenseRequestsSubject.asObservable()

  val user: Observable<User?>
    get() = userSubject.asObservable()

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

  fun onLogoutRequested() {
    logoutRequestsSubject.onNext(Unit)
  }

  fun onLicenseRequested() {
    licenseRequestsSubject.onNext(Unit)
  }
}
