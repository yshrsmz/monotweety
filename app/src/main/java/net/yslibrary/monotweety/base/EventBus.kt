package net.yslibrary.monotweety.base

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlin.reflect.KClass

class EventBus {

  private val bus = PublishSubject.create<Event>().toSerialized()

  fun emit(event: Event): Unit = bus.onNext(event)

  fun <R : Event> on(kClass: KClass<R>): Observable<R> = bus.ofType(kClass.java)

  fun <R : Event> on(kClass: KClass<R>, initialValue: R): Observable<R> = bus.ofType(kClass.java).startWith(initialValue)

  fun hasObservers() = bus.hasObservers()

  interface Event
}