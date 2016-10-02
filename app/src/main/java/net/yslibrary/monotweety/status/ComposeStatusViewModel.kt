package net.yslibrary.monotweety.status

import net.yslibrary.monotweety.status.domain.CheckStatusLength
import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import rx.schedulers.Schedulers

/**
 * Created by yshrsmz on 2016/10/02.
 */
class ComposeStatusViewModel(private val checkStatusLength: CheckStatusLength) {

  private val isSendableStatusSubject = BehaviorSubject<Boolean>(false)

  private val statusLengthSubject = BehaviorSubject<StatusLength>()

  val isSendableStatus: Observable<Boolean>
    get() = isSendableStatusSubject.asObservable()

  val statusLength: Observable<StatusLength>
    get() = statusLengthSubject.asObservable()

  fun onStatusUpdated(status: String) {
    checkStatusLength.execute(status)
        .subscribeOn(Schedulers.io())
        .subscribe {
          statusLengthSubject.onNext(StatusLength(it.valid, it.length))
          isSendableStatusSubject.onNext(it.valid)
        }
  }

  fun onSendStatus(status: String) {

  }

  fun onUpdateSendButton() {

  }

  fun onBackPressed() {

  }

  data class StatusLength(val valid: Boolean, val length: Int, val maxLength: Int = 140)
}