package net.yslibrary.monotweety.ui.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import androidx.annotation.AttrRes

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

fun Context.getIntValueFromAttribute(@AttrRes attrId: Int): Int {
    val tv = TypedValue()
    theme.resolveAttribute(attrId, tv, true)
    return tv.data
}

fun Context.getColorFromAttribute(@AttrRes attrId: Int): Int {
    val tv = TypedValue()
    theme.resolveAttribute(attrId, tv, true)
    val arr = obtainStyledAttributes(tv.data, intArrayOf(attrId))
    return try {
        arr.getColor(0, -1)
    } finally {
        arr.recycle()
    }
}
