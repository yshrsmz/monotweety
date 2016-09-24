package net.yslibrary.monotweety

import android.app.Application
import android.content.Context
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/09/24.
 */
open class App : Application() {

  companion object {
    fun appComponent(context: Context): AppComponent = get(context).appComponent
    fun get(context: Context): App = context.applicationContext as App
  }

  val appComponent: AppComponent by lazy {
    DaggerAppComponent.builder()
        .appModule(Modules.appModule(this))
        .build()
  }

  @field:[Inject]
  lateinit var lifecycleCallbacks: App.LifecycleCallbacks

  override fun onCreate() {
    super.onCreate()

    appComponent(this).inject(this)
    lifecycleCallbacks.onCreate()
  }

  override fun onTerminate() {
    super.onTerminate()
    lifecycleCallbacks.onTerminate()
  }

  interface LifecycleCallbacks {
    fun onCreate()
    fun onTerminate()
  }
}