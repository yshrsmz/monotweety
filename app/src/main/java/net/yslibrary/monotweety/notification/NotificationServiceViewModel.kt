package net.yslibrary.monotweety.notification

import com.twitter.sdk.android.core.TwitterApiException
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

  private val overlongStatusSubject = PublishSubject<OverlongStatus>()

  private val updateCompletedSubject = PublishSubject<Unit>()

  private val closeNotificationSubject = PublishSubject<Unit>()

  private val errorSubject = PublishSubject<String>()

  val overlongStatus: Observable<OverlongStatus>
    get() = overlongStatusSubject.asObservable()

  val updateCompleted: Observable<Unit>
    get() = updateCompletedSubject.asObservable()

  val closeNotification: Observable<Unit>
    get() = closeNotificationSubject.asObservable()

  val error: Observable<String>
    get() = errorSubject.asObservable()

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
          when (it) {
            is OverlongStatusException -> {
              overlongStatusSubject.onNext(OverlongStatus(it.status, it.length))
            }
            is TwitterApiException -> {
              errorSubject.onNext(it.errorMessage)
            }
            else -> {
              errorSubject.onNext(it.message)
            }
          }
        })
  }

  fun onShowTweetDialogCommand() {
    Timber.d("onShowTweetDialogCommnad")
  }

  data class OverlongStatus(val status: String, val length: Int)
}