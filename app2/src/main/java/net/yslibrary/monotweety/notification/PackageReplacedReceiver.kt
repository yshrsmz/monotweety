package net.yslibrary.monotweety.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import timber.log.Timber

class PackageReplacedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("package replaced ---")
        if (context.shouldStartService()) {
            ContextCompat.startForegroundService(
                context,
                NotificationService.callingIntent(context)
            )
        }
    }
}
