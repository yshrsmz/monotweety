package net.yslibrary.monotweety.notification

import net.yslibrary.monotweety.setting.domain.NotificationEnabledManager
import net.yslibrary.monotweety.status.domain.UpdateStatus
import rx.Observable
import rx.lang.kotlin.PublishSubject
import timber.log.Timber


/**
 * Created by yshrsmz on 2016/09/26.
 */
class NotificationServiceViewModel(private val notificationEnabledManager: NotificationEnabledManager,
                                   private val updateStatus: UpdateStatus) {

  private val tweetableTextSubject = PublishSubject<String?>()

  private val overlongTextSubject = PublishSubject<String?>()

  private val updateCompletedSubject = PublishSubject<Unit>()

  private val closeNotificationSubject = PublishSubject<Unit>()

  val tweetableText: Observable<String?>
    get() = tweetableTextSubject.asObservable()

  val overlongText: Observable<String?>
    get() = overlongTextSubject.asObservable()

  val updateCompleted: Observable<Unit>
    get() = updateCompletedSubject.asObservable()

  val closeNotification: Observable<Unit>
    get() = closeNotificationSubject.asObservable()

  fun onShowNotificationCommand() {
    Timber.d("onShowNotificationCommand")
  }

  fun onCloseNotificationCommand() {
    Timber.d("onCloseNotificationCommand")
    notificationEnabledManager.set(false)
    closeNotificationSubject.onNext(Unit)
  }

  fun onDirectTweetCommand(text: String) {
    Timber.d("onDirectTweetCommand: $text")
    updateStatus.execute(text)
        .subscribe({
          Timber.d("tweet completed")
          updateCompletedSubject.onNext(Unit)
        }, { throwable ->
          Timber.e(throwable, throwable.message)
        })
  }

  fun onShowTweetDialogCommand() {
    Timber.d("onShowTweetDialogCommnad")
  }
}