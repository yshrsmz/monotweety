package net.yslibrary.monotweety.setting

import net.yslibrary.monotweety.data.user.User
import net.yslibrary.monotweety.setting.domain.KeepDialogOpenManager
import net.yslibrary.monotweety.setting.domain.NotificationEnabledManager
import net.yslibrary.monotweety.user.domain.GetUser
import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import timber.log.Timber


/**
 * Created by yshrsmz on 2016/09/29.
 */

class SettingViewModel(private val notificationEnabledManager: NotificationEnabledManager,
                       private val getUser: GetUser,
                       private val keepDialogOpenManager: KeepDialogOpenManager) {

  private val userSubject = BehaviorSubject<User?>(null)

  val notificationEnabledChanged: Observable<Boolean>
    get() = notificationEnabledManager.get()

  val keepDialogOpen: Observable<Boolean>
    get() = keepDialogOpenedManager.get()

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
    keepDialogOpenedManager.set(enabled)
  }
}
