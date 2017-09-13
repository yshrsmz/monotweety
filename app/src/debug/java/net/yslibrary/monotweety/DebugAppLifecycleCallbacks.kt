package net.yslibrary.monotweety

import android.app.NotificationManager
import android.content.Context
import com.facebook.stetho.Stetho
import timber.log.Timber

class DebugAppLifecycleCallbacks(
    context: Context,
    notificationManager: NotificationManager
) : AppLifecycleCallbacks(context, notificationManager) {

  override fun onCreate() {
    super.onCreate()
    initStetho()
  }

  override fun initTimber() {
    Timber.plant(Timber.DebugTree())
  }

  fun initStetho() {
    Stetho.initializeWithDefaults(context)
  }
}