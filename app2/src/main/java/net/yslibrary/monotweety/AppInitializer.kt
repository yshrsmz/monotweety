package net.yslibrary.monotweety

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import net.yslibrary.monotweety.analytics.CrashReportingTree
import net.yslibrary.monotweety.notification.createNotificationChannel
import timber.log.Timber

interface AppInitializer {
    fun init(app: App)
}

open class AppInitializerImpl : AppInitializer {
    override fun init(app: App) {
        initLogger()
        initNotificationChannel(app)
    }

    open fun initLogger() {
        Timber.plant(CrashReportingTree())
    }

    private fun initNotificationChannel(context: Context) {
        createNotificationChannel(context, NotificationManagerCompat.from(context))
    }
}
