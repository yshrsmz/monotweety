package net.yslibrary.monotweety.status

import com.twitter.sdk.android.core.models.Tweet
import net.yslibrary.monotweety.setting.domain.AlwaysKeepDialogOpenedManager
import net.yslibrary.monotweety.status.domain.CheckStatusLength
import net.yslibrary.monotweety.status.domain.GetPreviousStatus
import net.yslibrary.monotweety.status.domain.UpdateStatus
import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject
import rx.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by yshrsmz on 2016/10/02.
 */
class ComposeStatusViewModel(status: String,
                             private val checkStatusLength: CheckStatusLength,
                             private val updateStatus: UpdateStatus,
                             private val getPreviousStatus: GetPreviousStatus,
                             private val alwaysKeepDialogOpenedManager: AlwaysKeepDialogOpenedManager) {

  private val isSendableStatusSubject = BehaviorSubject<Boolean>(false)

  private val statusLengthSubject = BehaviorSubject<StatusLength>()

  private val statusSubject = BehaviorSubject<String>(status)

  private val statusUpdatedSubject = PublishSubject<Unit>()

  private val keepDialogOpenedSubject = BehaviorSubject<Boolean>()

  private val tweetAsThreadSubject = BehaviorSubject<Boolean>(false)

  val isSendableStatus: Observable<Boolean>
    get() = isSendableStatusSubject.asObservable()

  val statusLength: Observable<StatusLength>
    get() = statusLengthSubject.asObservable()

  val status: Observable<String>
    get() = statusSubject.asObservable()

  val statusUpdated: Observable<Unit>
    get() = statusUpdatedSubject.asObservable()

  val keepDialogOpened: Observable<Boolean>
    get() = keepDialogOpenedSubject.asObservable()

  val tweetAsThread: Observable<Boolean>
    get() = tweetAsThreadSubject.asObservable()

  init {
    getPreviousStatus.execute()
        .subscribe {
          Timber.d("previous status: ${it?.id}")
        }

    alwaysKeepDialogOpenedManager.get().first()
        .subscribe { keepDialogOpenedSubject.onNext(it) }
  }

  fun onStatusUpdated(status: String) {
    checkStatusLength.execute(status)
        .subscribeOn(Schedulers.io())
        .subscribe {
          statusSubject.onNext(status)
          statusLengthSubject.onNext(StatusLength(it.valid, it.length))
          isSendableStatusSubject.onNext(it.valid)
        }
  }

  fun onSendStatus() {
    Observable.combineLatest(
        isSendableStatus.filter { it },
        tweetAsThread,
        getPreviousStatus.execute().first(),
        status,
        { sendable, asThread, previousTweet, status ->
          Pair<Tweet?, String>(if (asThread) previousTweet else null, status)
        })
        .first().toSingle()
        .flatMapCompletable { updateStatus.execute(it.second, it.first?.id) }
        .doOnCompleted {
          Timber.d("status updated - complete")
          statusUpdatedSubject.onNext(Unit)
        }
        .subscribe()
  }

  fun onKeepDialogOpenedChanged(keep: Boolean) {
    keepDialogOpenedSubject.onNext(keep)
  }

  fun onEnableThreadChanged(enable: Boolean) {
    tweetAsThreadSubject.onNext(enable)
    if (enable) {
      keepDialogOpenedSubject.onNext(true)
    }
  }

  fun onUpdateSendButton() {

  }

  fun onBackPressed() {

  }

  data class StatusLength(val valid: Boolean, val length: Int, val maxLength: Int = 140)
}