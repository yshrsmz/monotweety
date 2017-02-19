package net.yslibrary.monotweety.base

import android.app.Service
import android.content.Intent

fun Service.closeNotificationDrawer() {
  val intent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
  applicationContext.sendBroadcast(intent)
}