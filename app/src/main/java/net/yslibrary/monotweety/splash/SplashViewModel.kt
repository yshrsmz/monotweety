package net.yslibrary.monotweety.splash

import net.yslibrary.monotweety.login.domain.IsLoggedIn
import rx.Observable
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject

/**
 * Created by yshrsmz on 2016/09/27.
 */
class SplashViewModel(private val isLoggedIn: IsLoggedIn) {

  private val loggedInSubject = BehaviorSubject.create<Boolean>()

  val loggedIn: Observable<Boolean>
    get() = loggedInSubject.asObservable()

  init {
    isLoggedIn.execute()
        .subscribeOn(Schedulers.io())
        .subscribe { loggedInSubject.onNext(it) }
  }
}