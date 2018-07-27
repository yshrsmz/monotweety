package net.yslibrary.monotweety.base

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.SerialDisposable
import io.reactivex.functions.Consumer

class SkipUntilCompletedConsumer<T>(val doOnSubscribe: (T, () -> Unit) -> Unit) : Consumer<T> {
    private var loading: Boolean = false
    override fun accept(t: T) {
        if (loading) {
            return
        }
        loading = true
        doOnSubscribe(t) { loading = false }
    }
}

fun <T> Observable<T>.subscribeWhenCompleted(doOnSubscribe: (t: T, completed: () -> Unit) -> Unit): Disposable {
    return subscribe(SkipUntilCompletedConsumer(doOnSubscribe))
}

fun Disposable.setTo(disposable: SerialDisposable): Disposable {
    disposable.set(this)
    return this
}

fun <T> T.toSingle(): Single<T> = Single.just(this)
