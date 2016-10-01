package net.yslibrary.monotweety.notification

import net.yslibrary.monotweety.data.status.OverlongStatusException
import net.yslibrary.monotweety.setting.domain.NotificationEnabledManager
import net.yslibrary.monotweety.status.domain.CheckStatusLength
import net.yslibrary.monotweety.status.domain.UpdateStatus
import rx.Completable
import rx.Observable
import rx.lang.kotlin.PublishSubject
import timber.log.Timber


/**
 * Created by yshrsmz on 2016/09/26.
 */
class NotificationServiceViewModel(private val notificationEnabledManager: NotificationEnabledManager,
                                   private val checkStatusLength: CheckStatusLength,
                                   private val updateStatus: UpdateStatus) {

  private val tweetableTextSubject = PublishSubject<String?>()

  private val overlongTextSubject = PublishSubject<OverlongStatus>()

  private val updateCompletedSubject = PublishSubject<Unit>()

  private val closeNotificationSubject = PublishSubject<Unit>()

  val tweetableText: Observable<String?>
    get() = tweetableTextSubject.asObservable()

  val overlongText: Observable<OverlongStatus>
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
    checkStatusLength.execute(text)
        .flatMapCompletable {
          if (it.valid) {
            updateStatus.execute(text)
          } else {
            Completable.error(OverlongStatusException(status = it.status,
                length = it.length))
          }
        }
        .subscribe({
          Timber.d("tweet succeeded!")
          updateCompletedSubject.onNext(Unit)
        }, {
          if (it is OverlongStatusException) {
            overlongTextSubject.onNext(OverlongStatus(it.status, it.length))
          }
        })
  }

  fun onShowTweetDialogCommand() {
    Timber.d("onShowTweetDialogCommnad")
  }

  data class OverlongStatus(val status: String, val length: Int)
}