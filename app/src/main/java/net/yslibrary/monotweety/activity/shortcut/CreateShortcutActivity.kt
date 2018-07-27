package net.yslibrary.monotweety.activity.shortcut

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.pm.ShortcutInfoCompat
import android.support.v4.content.pm.ShortcutManagerCompat
import android.support.v4.graphics.drawable.IconCompat
import net.yslibrary.monotweety.R

class CreateShortcutActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shortcut = ShortcutActivity.callingIntent(applicationContext)
        shortcut.action = Intent.ACTION_EDIT

        val shortcutInfo = ShortcutInfoCompat.Builder(applicationContext, "open_editor")
            .setIcon(IconCompat.createWithResource(applicationContext, R.drawable.ic_app))
            .setLongLabel(getString(R.string.title_shortcut_editor))
            .setShortLabel(getString(R.string.title_shortcut_editor))
            .setIntent(shortcut)
            .build()

        val resultIntent = ShortcutManagerCompat.createShortcutResultIntent(applicationContext, shortcutInfo)

        setResult(RESULT_OK, resultIntent)
        finish()
    }
}
