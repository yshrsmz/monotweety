package net.yslibrary.monotweety

import net.yslibrary.monotweety.analytics.CrashReportingTree
import timber.log.Timber

interface AppInitializer {
    fun init(app: App)
}

open class AppInitializerImpl : AppInitializer {
    override fun init(app: App) {
        initLogger()
    }

    open fun initLogger() {
        Timber.plant(CrashReportingTree())
    }

    fun initNotificationChannel() {
        // TODO
    }
}
