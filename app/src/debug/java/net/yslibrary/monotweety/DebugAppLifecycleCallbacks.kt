package net.yslibrary.monotweety

import android.content.Context
import timber.log.Timber

/**
 * Created by yshrsmz on 2016/09/26.
 */
class DebugAppLifecycleCallbacks(context: Context) : AppLifecycleCallbacks(context) {
  override fun initTimber() {
    Timber.plant(Timber.DebugTree())
  }
}