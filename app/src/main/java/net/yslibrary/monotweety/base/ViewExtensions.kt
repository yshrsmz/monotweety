package net.yslibrary.monotweety.base

import android.app.Activity
import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bluelinelabs.conductor.Controller
import net.yslibrary.monotweety.GlideApp


/**
 * shortcut for findViewById
 */
@Suppress("UNCHECKED_CAST")
fun <T : View> View.findById(@IdRes id: Int): T = findViewById<T>(id)

@Suppress("UNCHECKED_CAST")
fun <T : View> ViewGroup.findById(@IdRes id: Int): T = findViewById<T>(id)

@Suppress("UNCHECKED_CAST")
fun <T : View> Activity.findById(@IdRes id: Int): T = findViewById<T>(id)

@Suppress("UNCHECKED_CAST")
fun <T : View> Controller.findById(@IdRes id: Int): T? = view?.findViewById<T>(id)

fun ImageView.load(url: String) {
    GlideApp.with(this)
        .load(url)
        .into(this)
}

fun ViewGroup.inflate(@LayoutRes resource: Int, root: ViewGroup = this, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(this.context).inflate(resource, root, attachToRoot)
}

fun Context.inflate(@LayoutRes resource: Int, root: ViewGroup? = null, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(this).inflate(resource, root, attachToRoot)
}
