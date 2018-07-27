package net.yslibrary.monotweety.login

import com.twitter.sdk.android.core.TwitterSession
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class LoginViewModel(private val loginCompletedSubject: PublishSubject<TwitterSession>) {

    private val loginFailedSubject = PublishSubject.create<Exception>()

    val loginCompleted: Observable<TwitterSession>
        get() = loginCompletedSubject

    val loginFailed: Observable<Exception>
        get() = loginFailedSubject

    fun onLoginCompleted(activeSession: TwitterSession) {
        loginCompletedSubject.onNext(activeSession)
    }

    fun onLoginFailed(exception: Exception) {
        loginFailedSubject.onNext(exception)
    }
}
