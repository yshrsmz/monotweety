package net.yslibrary.monotweety.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.rxlifecycle.RxController
import net.yslibrary.monotweety.base.di.Names
import net.yslibrary.rxeventbus.EventBus
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by yshrsmz on 2016/09/24.
 */
abstract class BaseController : RxController() {
  @field:[Inject Named(Names.FOR_ACTIVITY)]
  lateinit var activityBus: EventBus

  private var created: Boolean = false

  @Suppress("UNCHECKED_CAST")
  fun <PROVIDER> getComponentProvider(parent: Any): PROVIDER {
    return ((parent as HasComponent<Any>).component as PROVIDER)
  }

  override final fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    if (!created) {
      created = true
      onCreate()
    }

    return inflateView(inflater, container)
  }

  open fun onCreate() {

  }

  abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup): View

  fun <T> Observable<T>.bindToLifecycle(): Observable<T> = this.compose(this@BaseController.bindToLifecycle<T>())
}