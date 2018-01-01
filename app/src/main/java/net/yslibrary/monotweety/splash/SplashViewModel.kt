package net.yslibrary.monotweety.splash

import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import net.yslibrary.monotweety.login.domain.IsLoggedIn

class SplashViewModel(private val isLoggedIn: IsLoggedIn) {

  private val loggedInSubject = BehaviorSubject.create<Boolean>()

  val loggedIn: Observable<Boolean>
    get() = loggedInSubject

  init {
    isLoggedIn.execute()
        .subscribeOn(Schedulers.io())
        .subscribeBy { loggedInSubject.onNext(it) }
  }
}