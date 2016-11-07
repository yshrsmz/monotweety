package net.yslibrary.monotweety.notification

import com.twitter.sdk.android.core.TwitterApiException
import net.yslibrary.monotweety.data.status.OverlongStatusException
import net.yslibrary.monotweety.setting.domain.FooterStateManager
import net.yslibrary.monotweety.setting.domain.KeepOpenManager
import net.yslibrary.monotweety.setting.domain.NotificationEnabledManager
import net.yslibrary.monotweety.status.domain.CheckStatusLength
import net.yslibrary.monotweety.status.domain.ClearPreviousStatus
import net.yslibrary.monotweety.status.domain.UpdateStatus
import rx.Completable
import rx.Observable
import rx.lang.kotlin.PublishSubject
import timber.log.Timber


/**
 * Created by yshrsmz on 2016/09/26.
 */
class NotificationServiceViewModel(private val notificationEnabledManager: NotificationEnabledManager,
                                   private val keepOpenManager: KeepOpenManager,
                                   private val checkStatusLength: CheckStatusLength,
                                   private val updateStatus: UpdateStatus,
                                   private val clearPreviousStatus: ClearPreviousStatus,
                                   private val footerStateManager: FooterStateManager) {

  private val overlongStatusSubject = PublishSubject<OverlongStatus>()

  private val updateCompletedSubject = PublishSubject<Unit>()

  private val stopNotificationServiceSubject = PublishSubject<Unit>()

  private val errorSubject = PublishSubject<String>()

  val overlongStatus: Observable<OverlongStatus>
    get() = overlongStatusSubject.asObservable()

  val updateCompleted: Observable<Unit>
    get() = updateCompletedSubject.asObservable()

  val stopNotificationService: Observable<Unit>
    get() = stopNotificationServiceSubject.asObservable()

  val closeNotificationDrawer: Observable<Unit>
    get() = updateCompletedSubject
        .switchMap { keepOpenManager.get().first() }
        .filter { !it }
        .map { Unit }

  val footerState: Observable<FooterStateManager.FooterState>
    get() = footerStateManager.get()

  val error: Observable<String>
    get() = errorSubject.asObservable()

  fun onCloseNotificationCommand() {
    Timber.d("onCloseNotificationCommand")
    notificationEnabledManager.set(false)
    stopNotificationServiceSubject.onNext(Unit)
  }

  fun onDirectTweetCommand(text: String) {
    Timber.d("onDirectTweetCommand: $text")
    footerStateManager.get().first().toSingle()
        .map { (if (it.enabled) "$text ${it.text}" else text).trim() }
        .flatMap { checkStatusLength.execute(it) }
        .flatMapCompletable {
          if (it.valid) {
            updateStatus.execute(it.status)
          } else {
            Completable.error(OverlongStatusException(status = text.trim(), length = it.length))
          }
        }
        // clear previous status since tweet from notification does not support "chain tweet as a thread"
        .andThen(clearPreviousStatus.execute())
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

  data class OverlongStatus(val status: String, val length: Int)
}