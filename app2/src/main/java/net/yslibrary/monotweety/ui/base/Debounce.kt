package net.yslibrary.monotweety.ui.base

import android.view.View
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * https://medium.com/swlh/android-click-debounce-80b3f2e638f3
 */
fun <T> debounce(
    delayMillis: Long = 800,
    scope: CoroutineScope,
    action: (T) -> Unit,
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (debounceJob == null) {
            debounceJob = scope.launch {
                action(param)
                delay(delayMillis)
                debounceJob = null
            }
        }
    }
}

fun View.setDebounceClickListener(listener: (view: View) -> Unit) {
    val scope = ViewTreeLifecycleOwner.get(this)!!.lifecycleScope
    val clickWithDebounce: (view: View) -> Unit = debounce(scope = scope) {
        listener(it)
    }
    setOnClickListener(clickWithDebounce)
}
