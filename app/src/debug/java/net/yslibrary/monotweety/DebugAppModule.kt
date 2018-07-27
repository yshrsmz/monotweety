package net.yslibrary.monotweety

import android.app.NotificationManager
import android.content.Context

class DebugAppModule(context: Context) : AppModule(context) {

    override fun provideAppLifecycleCallbacks(context: Context, notificationManager: NotificationManager): App.LifecycleCallbacks {
        return DebugAppLifecycleCallbacks(context, notificationManager)
    }
}
