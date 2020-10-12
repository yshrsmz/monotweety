package net.yslibrary.monotweety

import android.app.Application
import android.content.Context
import com.codingfeline.twitter4kt.core.ConsumerKeys

class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(
            context = this,
            consumerKeys = getTwitterConsumerKeys()
        )
    }

    companion object {
        fun get(context: Context): App = context.applicationContext as App
        fun appComponent(context: Context): AppComponent = get(context).appComponent
    }
}

private fun getTwitterConsumerKeys(): ConsumerKeys {
    return ConsumerKeys(
        key = (BuildConfig.TWITTER_API_KEY_1 to BuildConfig.TWITTER_API_KEY_2).concatAlternately(),
        secret = (BuildConfig.TWITTER_API_SECRET_1 to BuildConfig.TWITTER_API_SECRET_2).concatAlternately()
    )
}
