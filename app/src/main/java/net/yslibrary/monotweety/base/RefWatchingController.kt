package net.yslibrary.monotweety.base

import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.bluelinelabs.conductor.rxlifecycle.RxController
import com.squareup.leakcanary.RefWatcher
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/10/13.
 */
abstract class RefWatchingController : RxController() {

  private var hasExisted: Boolean = false

  @set:[Inject]
  var refWatcher: RefWatcher? = null

  override fun onDestroy() {
    super.onDestroy()
    if (hasExisted) {
      refWatcher?.watch(this)
    }
  }

  override fun onChangeEnded(changeHandler: ControllerChangeHandler, changeType: ControllerChangeType) {
    super.onChangeEnded(changeHandler, changeType)

    hasExisted = !changeType.isEnter
    if (isDestroyed) {
      refWatcher?.watch(this)
    }
  }
}