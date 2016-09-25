package net.yslibrary.monotweety.base

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller


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