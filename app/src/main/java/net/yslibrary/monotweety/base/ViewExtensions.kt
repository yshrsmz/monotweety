package net.yslibrary.monotweety.base

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.bluelinelabs.conductor.Controller
import net.yslibrary.monotweety.GlideApp


/**
 * shortcut for findViewById
 */
@Suppress("UNCHECKED_CAST")
fun <T : View> View.findById(@IdRes id: Int): T = findViewById(id)

@Suppress("UNCHECKED_CAST")
fun <T : View> ViewGroup.findById(@IdRes id: Int): T = findViewById(id)

@Suppress("UNCHECKED_CAST")
fun <T : View> Activity.findById(@IdRes id: Int): T = findViewById(id)

@Suppress("UNCHECKED_CAST")
fun <T : View> Controller.findById(@IdRes id: Int): T? = view?.findViewById(id)

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

fun View.showKeyboard() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}
