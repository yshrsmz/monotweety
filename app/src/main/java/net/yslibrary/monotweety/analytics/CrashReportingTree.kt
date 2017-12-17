package net.yslibrary.monotweety.analytics

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

class CrashReportingTree : Timber.Tree() {
  override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
    if (priority == Log.VERBOSE || priority == Log.DEBUG) {
      return
    }

    Crashlytics.log(priority, tag, message)

    t?.let {
      if (priority == Log.ERROR || priority == Log.WARN) {
        Crashlytics.logException(it)
      }
    }
  }
}