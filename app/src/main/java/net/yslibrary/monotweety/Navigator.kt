package net.yslibrary.monotweety

import android.content.Intent
import android.net.Uri
import com.bluelinelabs.conductor.Router
import net.yslibrary.monotweety.base.BaseActivity

/**
 * Created by yshrsmz on 2016/10/09.
 */
class Navigator(private val activity: BaseActivity,
                private val router: Router) {

  fun toSplash() {

  }

  fun toLogin() {

  }

  fun toSetting() {

  }

  fun startLogoutService() {

  }

  fun startNotificationService() {

  }

  fun openExternalAppWithUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    activity.startActivity(intent)
  }
}