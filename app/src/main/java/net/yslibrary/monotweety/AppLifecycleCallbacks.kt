package net.yslibrary.monotweety

import android.app.NotificationManager
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import net.yslibrary.monotweety.analytics.CrashReportingTree
import net.yslibrary.monotweety.notification.createNotificationChannel
import net.yslibrary.monotweety.util.concatAlternately
import timber.log.Timber

open class AppLifecycleCallbacks(
    val context: Context,
    private val notificationManager: NotificationManager
) : App.LifecycleCallbacks {

    override fun onCreate() {
        initTimber()
        initNotificationChannel()
        initThreeTenABP()
        initTwitterKit()
    }

    override fun onTerminate() {

    }

    fun initNotificationChannel() {
        createNotificationChannel(context, notificationManager)
    }

    fun initThreeTenABP() {
        AndroidThreeTen.init(context)
    }

    open fun initTimber() {
        Timber.plant(CrashReportingTree())
    }

    fun initTwitterKit() {
        val authConfig = TwitterAuthConfig(
            concatAlternately(BuildConfig.TWITTER_API_KEY_1, BuildConfig.TWITTER_API_KEY_2),
            concatAlternately(BuildConfig.TWITTER_API_SECRET_1, BuildConfig.TWITTER_API_SECRET_2)
        )
        val twitterConfig = TwitterConfig.Builder(context)
            .twitterAuthConfig(authConfig)
            //.debug(true)
            .build()
        Twitter.initialize(twitterConfig)
    }
}
