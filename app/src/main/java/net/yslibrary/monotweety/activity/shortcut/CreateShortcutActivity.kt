package net.yslibrary.monotweety.activity.shortcut

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import net.yslibrary.monotweety.R

class CreateShortcutActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val shortcut = ShortcutActivity.callingIntent(applicationContext)

    val resultIntent = Intent()
    resultIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.title_shortcut_editor))
    val iconRes = Intent.ShortcutIconResource.fromContext(applicationContext, R.drawable.ic_app)
    resultIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes)
    resultIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcut)

    setResult(RESULT_OK, resultIntent)
    finish()
  }
}