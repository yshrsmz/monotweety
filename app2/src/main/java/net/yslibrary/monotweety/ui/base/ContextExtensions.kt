package net.yslibrary.monotweety.ui.base

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openExternalAppWithUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}

fun Context.openExternalAppWithShareIntent(text: String) {
    val intent = Intent(Intent.ACTION_SEND)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .putExtra(Intent.EXTRA_TEXT, text)
        .setType("text/plain")

    startActivity(intent)
}
