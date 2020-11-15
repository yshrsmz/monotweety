package net.yslibrary.monotweety.notification

import android.content.Context
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.yslibrary.monotweety.App

fun Context.shouldStartService(): Boolean {
    val observeSession = App.appComponent(this).observeSession()
    val observeSettings = App.appComponent(this).observeSettings()

    return runBlocking {
        val session = observeSession().first()
        val settings = observeSettings().first()

        session != null && settings.notificationEnabled
    }
}
