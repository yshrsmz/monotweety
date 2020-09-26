package net.yslibrary.monotweety.analytics

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val logLevel = LogLevel.fromPriority(priority)
        if (t != null) {
            FirebaseCrashlytics.getInstance().recordException(t)
        }

        when (logLevel) {
            LogLevel.ERROR,
            LogLevel.WARN,
            LogLevel.INFO -> {
                FirebaseCrashlytics.getInstance().log("${logLevel.name}/$tag: $message")
            }
            else -> Unit // no-op
        }
    }

    @Suppress("unused")
    private enum class LogLevel(val priority: Int) {
        VERBOSE(Log.VERBOSE),
        DEBUG(Log.DEBUG),
        INFO(Log.INFO),
        WARN(Log.WARN),
        ERROR(Log.ERROR),
        ASSERT(Log.ASSERT);

        companion object {
            fun fromPriority(priority: Int): LogLevel {
                return values().firstOrNull { it.priority == priority } ?: DEBUG
            }
        }
    }
}
