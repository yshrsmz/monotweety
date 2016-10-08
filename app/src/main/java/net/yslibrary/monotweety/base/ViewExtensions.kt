package net.yslibrary.monotweety.base

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bluelinelabs.conductor.Controller
import com.bumptech.glide.Glide


/**
 * shortcut for findViewById
 */
@Suppress("UNCHECKED_CAST")
fun <T> View.findById(@IdRes id: Int): T = findViewById(id) as T

@Suppress("UNCHECKED_CAST")
fun <T> ViewGroup.findById(@IdRes id: Int): T = findViewById(id) as T

@Suppress("UNCHECKED_CAST")
fun <T> Activity.findById(@IdRes id: Int): T = findViewById(id) as T

@Suppress("UNCHECKED_CAST")
fun <T> Controller.findById(@IdRes id: Int): T = view.findViewById(id) as T

fun ImageView.load(url: String) {
  Glide.with(this.context)
      .load(url)
      .into(this)
}