package net.yslibrary.monotweety

import net.yslibrary.monotweety.analytics.CrashReportingTree
import timber.log.Timber

class DebugAppInitializer : AppInitializerImpl() {
    override fun initLogger() {
        Timber.plant(CrashReportingTree(), Timber.DebugTree())
    }
}
