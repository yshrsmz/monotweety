package net.yslibrary.monotweety.ui.base

import android.content.Context
import android.net.Uri
import android.webkit.URLUtil
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun Context.navigateToBrowser(url: String) {
    if (!url.isValidUrl()) return

    CustomTabsIntent.Builder()
        .build()
        .launchUrl(this, Uri.parse(url))
}

/**
 * Check if the current destination can handle the provided [direction].
 *
 * https://stackoverflow.com/questions/51060762/java-lang-illegalargumentexception-navigation-destination-xxx-is-unknown-to-thi
 */
fun NavController.navigateSafe(direction: NavDirections) {
    val action =
        currentDestination?.getAction(direction.actionId) ?: graph.getAction(direction.actionId)
    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(direction)
    }
}

fun String.isValidUrl(): Boolean = URLUtil.isHttpUrl(this) || URLUtil.isHttpsUrl(this)
