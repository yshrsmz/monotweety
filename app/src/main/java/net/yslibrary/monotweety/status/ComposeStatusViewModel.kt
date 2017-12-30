package net.yslibrary.monotweety.status

import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.twitter.sdk.android.core.TwitterApiException
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import net.yslibrary.monotweety.Config
import net.yslibrary.monotweety.data.status.Tweet
import net.yslibrary.monotweety.setting.domain.FooterStateManager
import net.yslibrary.monotweety.setting.domain.KeepOpenManager
import net.yslibrary.monotweety.status.domain.CheckStatusLength
import net.yslibrary.monotweety.status.domain.ClearPreviousStatus
import net.yslibrary.monotweety.status.domain.GetPreviousStatus
import net.yslibrary.monotweety.status.domain.UpdateStatus
import timber.log.Timber

class ComposeStatusViewModel(status: String,
                             private val config: Config,
                             private val checkStatusLength: CheckStatusLength,
                             private val updateStatus: UpdateStatus,
                             private val getPreviousStatus: GetPreviousStatus,
                             private val clearPreviousStatus: ClearPreviousStatus,
                             private val keepOpenManager: KeepOpenManager,
                             private val footerStateManager: FooterStateManager) {

  private val isSendableStatusSubject = BehaviorSubject.createDefault(false)

  private val statusInfoSubject = BehaviorSubject.create<StatusInfo>()

  private val statusUpdatedSubject = PublishSubject.create<Unit>()

  private val keepOpenSubject = BehaviorSubject.create<Boolean>()

  private val tweetAsThreadSubject = BehaviorSubject.createDefault(false)

  private val progressEventsSubject = BehaviorSubject.createDefault(ProgressEvent.FINISHED)

  private val messagesSubject = PublishSubject.create<String>()

  private val allowCloseViewSubject = BehaviorSubject.createDefault(false)

  private val previousStatusSubject = BehaviorSubject.createDefault<Optional<Tweet>>(None)

  val isSendableStatus: Observable<Boolean>
    get() = isSendableStatusSubject

  val statusInfo: Observable<StatusInfo>
    get() = statusInfoSubject

  val statusUpdated: Observable<Unit>
    get() = statusUpdatedSubject

  val keepOpen: Observable<Boolean>
    get() = keepOpenSubject

  val tweetAsThread: Observable<Boolean>
    get() = tweetAsThreadSubject

  val progressEvents: Observable<ProgressEvent>
    get() = progressEventsSubject

  val messages: Observable<String>
    get() = messagesSubject

  val allowCloseView: Observable<Boolean>
    get() = allowCloseViewSubject

  val statusMaxLength: Single<Int>
    get() = Single.just(config.statusMaxLength)

  val previousStatus: Observable<Optional<Tweet>>
    get() = previousStatusSubject

  val closeViewRequests: Observable<Unit>
    get() {
      return statusUpdatedSubject
          .switchMapSingle { keepOpenSubject.firstOrError() }
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
        .filter { it.toNullable() != null }
        .subscribe {
          Timber.d("previous status: ${it.toNullable()?.text}")
          previousStatusSubject.onNext(it)
        }

    keepOpenManager.get().firstElement()
        .subscribe { keepOpenSubject.onNext(it) }

    footerStateManager.get().firstElement()
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
        .subscribeBy {
          statusInfoSubject.onNext(StatusInfo(status, it.valid, it.length, it.maxLength))

          isSendableStatusSubject.onNext(it.valid)
        }
  }

  fun onSendStatus() {
    Observable.combineLatest(
        isSendableStatus.filter { it },
        tweetAsThread,
        getPreviousStatus.execute().firstOrError(),
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