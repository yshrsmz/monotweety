package net.yslibrary.monotweety

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import io.fabric.sdk.android.Fabric
import net.yslibrary.monotweety.analytics.CrashReportingTree
import timber.log.Timber

/**
 * Created by yshrsmz on 2016/09/24.
 */
open class AppLifecycleCallbacks(val context: Context) : App.LifecycleCallbacks {
  override fun onCreate() {
    initTimber()
    initThreeTenABP()
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

  fun initFabric() {
    val authConfig = TwitterAuthConfig(BuildConfig.TWITTER_API_KEY, BuildConfig.TWITTER_API_SECRET)
    val fabric = Fabric.Builder(context)
        .kits(Twitter(authConfig), Crashlytics())
//        .debuggable(true)
        .build()

    Fabric.with(fabric)
  }
}