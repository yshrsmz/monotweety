package net.yslibrary.monotweety

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import io.fabric.sdk.android.Fabric

/**
 * Created by yshrsmz on 2016/09/24.
 */
open class AppLifecycleCallbacks(val context: Context) : App.LifecycleCallbacks {
  override fun onCreate() {
    initFabric()
  }

  override fun onTerminate() {

  }

  fun initFabric() {
    val authConfig = TwitterAuthConfig(BuildConfig.TWITTER_API_KEY, BuildConfig.TWITTER_API_SECRET)
    Fabric.with(context, Twitter(authConfig), Crashlytics())
  }
}