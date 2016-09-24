package net.yslibrary.monotweety.activity.main

import android.os.Bundle
import com.bluelinelabs.conductor.ChangeHandlerFrameLayout
import com.bluelinelabs.conductor.Controller
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.BaseActivity

class MainActivity : BaseActivity() {
  override val container: ChangeHandlerFrameLayout
    get() = findViewById(R.id.controller_container) as ChangeHandlerFrameLayout

  override val layoutResId: Int
    get() = R.layout.activity_main

  override val rootController: Controller
    get() = throw UnsupportedOperationException()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}
