package net.yslibrary.monotweety.splash

import net.yslibrary.monotweety.login.domain.IsLoggedIn
import rx.Observable
import rx.subjects.BehaviorSubject

/**
 * Created by yshrsmz on 2016/09/27.
 */
class SplashViewModel(private val isLoggedIn: IsLoggedIn) {

  val loggedInSubject = BehaviorSubject.create<Boolean>()

  val loggedIn: Observable<Boolean>
    get() = loggedInSubject.asObservable()

  init {
    isLoggedIn.execute()
        .subscribe { loggedInSubject.onNext(it) }
  }
}