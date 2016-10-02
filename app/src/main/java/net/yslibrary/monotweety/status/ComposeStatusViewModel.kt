package net.yslibrary.monotweety.status

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
                             private val getPreviousStatus: GetPreviousStatus) {

  private val isSendableStatusSubject = BehaviorSubject<Boolean>(false)

  private val statusLengthSubject = BehaviorSubject<StatusLength>()

  private val statusSubject = BehaviorSubject<String>(status)

  private val statusUpdatedSubject = PublishSubject<Unit>()

  val isSendableStatus: Observable<Boolean>
    get() = isSendableStatusSubject.asObservable()

  val statusLength: Observable<StatusLength>
    get() = statusLengthSubject.asObservable()

  val status: Observable<String>
    get() = statusSubject.asObservable()

  val statusUpdated: Observable<Unit>
    get() = statusUpdatedSubject.asObservable()

  init {
    getPreviousStatus.execute()
        .subscribe { Timber.d("previous status: ${it?.id}") }
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
        status,
        { sendable, status -> status }).first().toSingle()
        .flatMapCompletable { status ->
          getPreviousStatus.execute().first().toSingle()
              .flatMapCompletable { updateStatus.execute(status, it?.id) }
        }
        .doOnCompleted {
          Timber.d("status updated - complete")
          statusUpdatedSubject.onNext(Unit)
        }
        .subscribe()
  }

  fun onUpdateSendButton() {

  }

  fun onBackPressed() {

  }

  data class StatusLength(val valid: Boolean, val length: Int, val maxLength: Int = 140)
}