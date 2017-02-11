package net.yslibrary.monotweety.base

import com.bluelinelabs.conductor.ControllerChangeType
import com.squareup.leakcanary.RefWatcher

interface RefWatcherDelegate {
  fun takeRefWatcher(refWatcher: RefWatcher?)
  fun onDestroy()
  fun onChangeEnded(isDestroyed: Boolean, changeType: ControllerChangeType)
}

class RefWatcherDelegateImpl() : RefWatcherDelegate {

  private var hasExisted: Boolean = false

  private var refWatcher: RefWatcher? = null

  override fun takeRefWatcher(refWatcher: RefWatcher?) {
    this.refWatcher = refWatcher
  }

  override fun onDestroy() {
    if (hasExisted) {
      refWatcher?.watch(this)
    }
  }

  override fun onChangeEnded(isDestroyed: Boolean, changeType: ControllerChangeType) {

    hasExisted = !changeType.isEnter
    if (isDestroyed) {
      refWatcher?.watch(this)
    }
  }
}