package net.yslibrary.monotweety.base

import com.bluelinelabs.conductor.rxlifecycle.RxController
import net.yslibrary.monotweety.ForActivity
import net.yslibrary.rxeventbus.EventBus
import rx.Observable
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/09/24.
 */
abstract class BaseController : RxController() {
  @field:[Inject ForActivity]
  lateinit var activityBus: EventBus

  @Suppress("UNCHECKED_CAST")
  fun <PROVIDER> getComponentProvider(parent: Any): PROVIDER {
    return ((parent as HasComponent<Any>).component as PROVIDER)
  }

  fun <T> Observable<T>.bindToLifecycle(): Observable<T> = this.compose(this@BaseController.bindToLifecycle<T>())
}