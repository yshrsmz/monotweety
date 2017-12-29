package net.yslibrary.monotweety.base

import io.reactivex.Single
import rx.Observable
import rx.Subscription
import rx.functions.Action1
import rx.subscriptions.SerialSubscription

class SkipUntilCompletedAction1<T>(val doOnSubscribe: (T, () -> Unit) -> Unit) : Action1<T> {
  private var loading: Boolean = false
  override fun call(t: T) {
    if (loading) {
      return
    }
    loading = true
    doOnSubscribe(t) { loading = false }
  }
}

fun <T> Observable<T>.subscribeWhenCompleted(doOnSubscribe: (t: T, completed: () -> Unit) -> Unit): Subscription {
  return subscribe(SkipUntilCompletedAction1(doOnSubscribe))
}

fun Subscription.setTo(subscription: SerialSubscription): Subscription {
  subscription.set(this)
  return this
}

fun <T> T.toSingle(): Single<T> = Single.just(this)