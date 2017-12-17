package net.yslibrary.monotweety.status

import com.twitter.sdk.android.core.TwitterApiException

import net.yslibrary.monotweety.Config
import net.yslibrary.monotweety.data.status.Tweet
import net.yslibrary.monotweety.setting.domain.FooterStateManager
import net.yslibrary.monotweety.setting.domain.KeepOpenManager
import net.yslibrary.monotweety.status.domain.CheckStatusLength
import net.yslibrary.monotweety.status.domain.ClearPreviousStatus
import net.yslibrary.monotweety.status.domain.GetPreviousStatus
import net.yslibrary.monotweety.status.domain.UpdateStatus
import rx.Completable
import rx.Observable
import rx.Single
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import timber.log.Timber

class ComposeStatusViewModel(status: String,
                             private val config: Config,
                             private val checkStatusLength: CheckStatusLength,
                             private val updateStatus: UpdateStatus,
                             private val getPreviousStatus: GetPreviousStatus,
                             private val clearPreviousStatus: ClearPreviousStatus,
                             private val keepOpenManager: KeepOpenManager,
                             private val footerStateManager: FooterStateManager) {

  private val isSendableStatusSubject = BehaviorSubject.create(false)

  private val statusInfoSubject = BehaviorSubject.create<StatusInfo>()

  private val statusUpdatedSubject = PublishSubject.create<Unit>()

  private val keepOpenSubject = BehaviorSubject.create<Boolean>()

  private val tweetAsThreadSubject = BehaviorSubject.create(false)

  private val progressEventsSubject = BehaviorSubject.create(ProgressEvent.FINISHED)

  private val messagesSubject = PublishSubject.create<String>()

  private val allowCloseViewSubject = BehaviorSubject.create(false)

  private val previousStatusSubject = BehaviorSubject.create(null as Tweet?)

  val isSendableStatus: Observable<Boolean>
    get() = isSendableStatusSubject.asObservable()

  val statusInfo: Observable<StatusInfo>
    get() = statusInfoSubject.asObservable()

  val statusUpdated: Observable<Unit>
    get() = statusUpdatedSubject.asObservable()

  val keepOpen: Observable<Boolean>
    get() = keepOpenSubject.asObservable()

  val tweetAsThread: Observable<Boolean>
    get() = tweetAsThreadSubject.asObservable()

  val progressEvents: Observable<ProgressEvent>
    get() = progressEventsSubject.asObservable()

  val messages: Observable<String>
    get() = messagesSubject.asObservable()

  val allowCloseView: Observable<Boolean>
    get() = allowCloseViewSubject.asObservable()

  val statusMaxLength: Single<Int>
    get() = Single.just(config.statusMaxLength)

  val previousStatus: Observable<Tweet?>
    get() = previousStatusSubject.asObservable()

  val closeViewRequests: Observable<Unit>
    get() {
      return statusUpdatedSubject
          .switchMap { keepOpenSubject.first() }
          .filter { !it }
          .map { Unit }
    }

  val footerState: Observable<FooterStateManager.State>
    get() = footerStateManager.get()

  val canClose: Boolean
    get() {
      val isSending = progressEventsSubject.value == ProgressEvent.IN_PROGRESS
      val hasContent = statusInfoSubject.value.length > 0
      val allowCloseView = allowCloseViewSubject.value

      return !isSending && (!hasContent || allowCloseView)
    }

  init {
    getPreviousStatus.execute()
        .subscribe {
          Timber.d("previous status: ${it?.text}")
          previousStatusSubject.onNext(it)
        }

    keepOpenManager.get().first()
        .subscribe { keepOpenSubject.onNext(it) }

    footerStateManager.get().first()
        .subscribe {
          val statusText = if (it.enabled)
            "$status ${it.text}"
          else
            status
          onStatusChanged(statusText)
        }
  }

  fun onStatusChanged(status: String) {
    checkStatusLength.execute(status)
        .subscribeOn(Schedulers.io())
        .subscribe {
          statusInfoSubject.onNext(StatusInfo(status, it.valid, it.length, it.maxLength))

          isSendableStatusSubject.onNext(it.valid)
        }
  }

  fun onSendStatus() {
    Observable.combineLatest(
        isSendableStatus.filter { it },
        tweetAsThread,
        getPreviousStatus.execute().first(),
        statusInfo,
        { _, asThread, previousTweet, (status) ->
          // return previous tweet and current status string
          Pair(if (asThread) previousTweet else null, status)
        })
        .first().toSingle()
        .doOnSuccess { progressEventsSubject.onNext(ProgressEvent.IN_PROGRESS) }
        .flatMapCompletable { updateStatus.execute(it.second, it.first?.id) }
        .andThen(tweetAsThread.first().toSingle()
            .flatMapCompletable { asThread ->
              if (asThread) {
                Completable.complete()
              } else {
                clearPreviousStatus.execute()
              }
            })
        .doOnCompleted {
          Timber.d("status updated - complete")
          statusUpdatedSubject.onNext(Unit)
        }
        .doOnTerminate { progressEventsSubject.onNext(ProgressEvent.FINISHED) }
        .subscribe({}, { t ->
          Timber.e(t, t.message)
          if (t is TwitterApiException && t.errorMessage != null) {
            messagesSubject.onNext(t.errorMessage)
          } else {
            messagesSubject.onNext("Something wrong happened")
          }
        })
  }

  fun onKeepOpenChanged(keep: Boolean) {
    keepOpenSubject.onNext(keep)
  }

  fun onEnableThreadChanged(enable: Boolean) {
    tweetAsThreadSubject.onNext(enable)
    if (enable) {
      keepOpenSubject.onNext(true)
    }
  }

  fun onConfirmCloseView(allowCloseView: Boolean) {
    allowCloseViewSubject.onNext(allowCloseView)
  }

  fun onDestroy() {
    clearPreviousStatus.execute()
        .subscribe({ Timber.d("previous status cleared") })
  }

  data class StatusInfo(val status: String, val valid: Boolean, val length: Int, val maxLength: Int)

  enum class ProgressEvent {
    IN_PROGRESS,
    FINISHED
  }
}