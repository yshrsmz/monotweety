package net.yslibrary.monotweety.ui.base

import android.view.View
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

inline fun View.setDebounceClickListener(
    timeoutMillis: Long = 800,
    crossinline listener: (View) -> Unit,
) {
    setOnClickListener(object : View.OnClickListener {
        private var throttling = false

        override fun onClick(view: View) {
            if (throttling) return

            throttling = true
            view.postDelayed({ throttling = false }, timeoutMillis)
            listener(view)
        }
    })
}
