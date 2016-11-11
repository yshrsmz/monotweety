package net.yslibrary.monotweety.activity.shortcut

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import net.yslibrary.monotweety.App
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.activity.compose.ComposeActivity

/**
 * Created by yshrsmz on 2016/11/10.
 */
class ShortcutActivity : Activity() {

  companion object {
    fun callingIntent(context: Context): Intent {
      return Intent(context, ShortcutActivity::class.java)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val loggedIn = App.appComponent(this).isLoggedIn().execute().toBlocking().value()

    if (!loggedIn) {
      Toast.makeText(this, R.string.error_not_authorized, Toast.LENGTH_SHORT).show()
      finish()
      return
    }

    startActivity(ComposeActivity.callingIntent(this))
    finish()
  }
}