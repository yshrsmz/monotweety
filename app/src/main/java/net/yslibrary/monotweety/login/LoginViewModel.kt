package net.yslibrary.monotweety.login

import com.twitter.sdk.android.core.TwitterSession
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.subjects.PublishSubject

/**
 * Created by yshrsmz on 2016/09/27.
 */
class LoginViewModel(private val loginCompletedSubject: PublishSubject<TwitterSession>) {

  private val loginFailedSubject = PublishSubject<Exception>()

  val loginCompleted: Observable<TwitterSession>
    get() = loginCompletedSubject.asObservable()

  val loginFailed: Observable<Exception>
    get() = loginFailedSubject.asObservable()

  fun onLoginCompleted(activeSession: TwitterSession) {
    loginCompletedSubject.onNext(activeSession)
  }

  fun onLoginFailed(exception: Exception) {
    loginFailedSubject.onNext(exception)
  }
}