package net.yslibrary.monotweety

import android.app.NotificationManager
import android.content.Context
import timber.log.Timber

class DebugAppLifecycleCallbacks(
    context: Context,
    notificationManager: NotificationManager
) : AppLifecycleCallbacks(context, notificationManager) {

    override fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}
