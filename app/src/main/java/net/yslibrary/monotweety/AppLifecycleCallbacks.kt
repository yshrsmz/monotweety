package net.yslibrary.monotweety

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import io.fabric.sdk.android.Fabric
import net.yslibrary.monotweety.analytics.CrashReportingTree
import timber.log.Timber

open class AppLifecycleCallbacks(val context: Context) : App.LifecycleCallbacks {
  override fun onCreate() {
    initTimber()
    initThreeTenABP()
    initTwitterKit()
    initFabric()
  }

  override fun onTerminate() {

  }

  fun initThreeTenABP() {
    AndroidThreeTen.init(context)
  }

  open fun initTimber() {
    Timber.plant(CrashReportingTree())
  }

  fun initTwitterKit() {
    val authConfig = TwitterAuthConfig(BuildConfig.TWITTER_API_KEY, BuildConfig.TWITTER_API_SECRET)
    val twitterConfig = TwitterConfig.Builder(context)
        .twitterAuthConfig(authConfig)
        //.debug(true)
        .build()
    Twitter.initialize(twitterConfig)
  }

  fun initFabric() {

    val fabric = Fabric.Builder(context)
        .kits(Crashlytics())
//        .debuggable(true)
        .build()

    Fabric.with(fabric)
  }
}