package net.yslibrary.monotweety.notification

import com.twitter.sdk.android.core.TwitterApiException
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function3
import io.reactivex.subjects.PublishSubject
import net.yslibrary.monotweety.data.appinfo.AppInfo
import net.yslibrary.monotweety.data.status.OverlongStatusException
import net.yslibrary.monotweety.setting.domain.FooterStateManager
import net.yslibrary.monotweety.setting.domain.KeepOpenManager
import net.yslibrary.monotweety.setting.domain.NotificationEnabledManager
import net.yslibrary.monotweety.setting.domain.SelectedTimelineAppInfoManager
import net.yslibrary.monotweety.status.domain.CheckStatusLength
import net.yslibrary.monotweety.status.domain.ClearPreviousStatus
import net.yslibrary.monotweety.status.domain.UpdateStatus
import timber.log.Timber

class NotificationServiceViewModel(private val notificationEnabledManager: NotificationEnabledManager,
                                   private val keepOpenManager: KeepOpenManager,
                                   private val checkStatusLength: CheckStatusLength,
                                   private val updateStatus: UpdateStatus,
                                   private val clearPreviousStatus: ClearPreviousStatus,
                                   private val footerStateManager: FooterStateManager,
                                   private val selectedTimelineAppInfoManager: SelectedTimelineAppInfoManager) {

  private val overlongStatusSubject = PublishSubject.create<OverlongStatus>()

  private val updateCompletedSubject = PublishSubject.create<Unit>()

  private val stopNotificationServiceSubject = PublishSubject.create<Unit>()

  private val updateNotificatoinRequestsSubject = PublishSubject.create<Unit>()

  private val errorSubject = PublishSubject.create<String>()

  val overlongStatus: Observable<OverlongStatus>
    get() = overlongStatusSubject

  val updateCompleted: Observable<Unit>
    get() = updateCompletedSubject

  val stopNotificationService: Observable<Unit>
    get() = stopNotificationServiceSubject

  val closeNotificationDrawer: Observable<Unit>
    get() = updateCompletedSubject
        .switchMapSingle { keepOpenManager.get().firstOrError() }
        .filter { !it }
        .map { Unit }

  val updateNotificationRequests: Observable<NotificationInfo>
    get() = Observable.combineLatest(
        updateNotificatoinRequestsSubject.startWith(Unit),
        footerStateManager.get(),
        selectedTimelineAppInfoManager.get(),
        Function3 { _: Unit, footerState: FooterStateManager.State, appInfo: AppInfo -> NotificationInfo(footerState, appInfo) })

  val footerState: Observable<FooterStateManager.State>
    get() = footerStateManager.get()

  val selectedTimelineApp: Observable<AppInfo>
    get() = selectedTimelineAppInfoManager.get()

  val error: Observable<String>
    get() = errorSubject

  fun onCloseNotificationCommand() {
    Timber.d("onCloseNotificationCommand")
    notificationEnabledManager.set(false)
    stopNotificationServiceSubject.onNext(Unit)
  }

  fun onDirectTweetCommand(text: String) {
    Timber.d("onDirectTweetCommand: $text")
    footerStateManager.get().firstOrError()
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
              errorSubject.onNext(it.message ?: "")
            }
          }
        })
  }

  fun onUpdateNotificationRequested() {
    updateNotificatoinRequestsSubject.onNext(Unit)
  }

  data class OverlongStatus(val status: String, val length: Int)

  data class NotificationInfo(val footerState: FooterStateManager.State,
                              val timelineApp: AppInfo)
}