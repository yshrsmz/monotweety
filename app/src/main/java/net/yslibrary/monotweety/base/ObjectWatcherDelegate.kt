package net.yslibrary.monotweety.base

import com.bluelinelabs.conductor.ControllerChangeType
import leakcanary.ObjectWatcher
import javax.inject.Inject

interface ObjectWatcherDelegate {
    fun handleOnDestroy()
    fun handleOnChangeEnded(isDestroyed: Boolean, changeType: ControllerChangeType)
}

class ObjectWatcherDelegateImpl @Inject constructor(
    private val objectWatcher: ObjectWatcher
) : ObjectWatcherDelegate {

    private var hasExisted: Boolean = false

    override fun handleOnDestroy() {
        if (hasExisted) {
            objectWatcher.watch(this, "check Controller leak")
        }
    }

    override fun handleOnChangeEnded(isDestroyed: Boolean, changeType: ControllerChangeType) {

        hasExisted = !changeType.isEnter
        if (isDestroyed) {
            objectWatcher.watch(this, "check Controller leak")
        }
    }
}
