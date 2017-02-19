package net.yslibrary.monotweety

import android.content.Context
import timber.log.Timber

class DebugAppLifecycleCallbacks(context: Context) : AppLifecycleCallbacks(context) {
  override fun initTimber() {
    Timber.plant(Timber.DebugTree())
  }
}