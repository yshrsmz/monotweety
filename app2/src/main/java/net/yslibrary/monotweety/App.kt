package net.yslibrary.monotweety

import android.app.Application
import android.content.Context
import com.codingfeline.twitter4kt.core.ConsumerKeys

class App : Application() {
    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(
            context = this,
            consumerKeys = getTwitterConsumerKeys()
        )
    }

    private var userComponent: UserComponent? = null

    override fun onCreate() {
        super.onCreate()
        AppInitializerProvider.get().init(this)
    }

    companion object {
        fun get(context: Context): App = context.applicationContext as App
        fun appComponent(context: Context): AppComponent = get(context).appComponent
        fun userComponent(context: Context): UserComponent {
            val app = get(context)

            return app.userComponent ?: kotlin.run {
                val userComponent = app.appComponent.userComponent().build()
                app.userComponent = userComponent
                userComponent
            }
        }

        fun clearUserComponent(context: Context) {
            get(context).userComponent = null
        }
    }
}

private fun getTwitterConsumerKeys(): ConsumerKeys {
    return ConsumerKeys(
        key = (BuildConfig.TWITTER_API_KEY_1 to BuildConfig.TWITTER_API_KEY_2).concatAlternately(),
        secret = (BuildConfig.TWITTER_API_SECRET_1 to BuildConfig.TWITTER_API_SECRET_2).concatAlternately()
    )
}
