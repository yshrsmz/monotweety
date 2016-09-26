package net.yslibrary.monotweety

import android.app.Application
import android.content.Context
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/09/24.
 */
open class App : Application() {

  companion object {
    fun get(context: Context): App = context.applicationContext as App
    fun appComponent(context: Context): AppComponent = get(context).appComponent
    fun userComponent(context: Context): UserComponent {
      val app = get(context)

      if (app.userComponent == null) {
        app.userComponent = app.appComponent.userComponent()
      }

      return app.userComponent!!
    }
  }

  val appComponent: AppComponent by lazy {
    DaggerAppComponent.builder()
        .appModule(Modules.appModule(this))
        .build()
  }

  var userComponent: UserComponent? = null

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