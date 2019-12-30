package net.yslibrary.monotweety

import android.app.Application
import android.content.Context
import leakcanary.ObjectWatcher
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

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

        /**
         * clear UserComponent
         * this method should be called when user is logging out
         */
        fun clearUserComponent(context: Context) {
            get(context).userComponent = null
        }
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(Modules.appModule(this))
            .build()
    }

    var userComponent: UserComponent? = null

    @set:[Inject]
    var lifecycleCallbacks by Delegates.notNull<App.LifecycleCallbacks>()

    // inject here just to make sure that LeakCanary is initialized
    @set:[Inject]
    var refWatcher by Delegates.notNull<ObjectWatcher>()

    override fun onCreate() {
        super.onCreate()

        appComponent(this).inject(this)
        lifecycleCallbacks.onCreate()

        Timber.d("App#onCreate")
    }

    override fun onTerminate() {
        super.onTerminate()
        Timber.d("App#onTerminate")
        lifecycleCallbacks.onTerminate()
    }

    // FIXME: https://youtrack.jetbrains.com/issue/KT-14306
// use ApplicationLifecycleCallbacks for now
    interface LifecycleCallbacks {
        fun onCreate()
        fun onTerminate()
    }
}
