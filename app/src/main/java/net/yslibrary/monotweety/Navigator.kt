package net.yslibrary.monotweety

import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import net.yslibrary.monotweety.base.BaseActivity
import net.yslibrary.monotweety.logout.LogoutService
import net.yslibrary.monotweety.notification.NotificationService

class Navigator(private val activity: BaseActivity) {

    fun toSplash() {

    }

    fun toLogin() {

    }

    fun toSetting() {

    }

    fun startLogoutService() {
        activity.startService(LogoutService.callingIntent(activity))
    }

    fun startNotificationService() {
        ContextCompat.startForegroundService(activity, NotificationService.callingIntent(activity))
    }

    fun stopNotificationService() {
        activity.stopService(NotificationService.callingIntent(activity))
    }

    fun openExternalAppWithUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity.startActivity(intent)
    }

    fun openProfileWithTwitterApp(name: String) {
        openExternalAppWithUrl("https://twitter.com/$name")
    }

    fun openExternalAppWithShareIntent(message: String) {
        val intent = Intent(Intent.ACTION_SEND)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra(Intent.EXTRA_TEXT, message)
            .setType("text/plain")

        activity.startActivity(intent)
    }
}
