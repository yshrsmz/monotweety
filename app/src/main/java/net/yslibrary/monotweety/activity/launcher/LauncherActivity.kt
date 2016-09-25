package net.yslibrary.monotweety.activity.launcher

import com.bluelinelabs.conductor.ChangeHandlerFrameLayout
import com.bluelinelabs.conductor.Controller
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.BaseActivity
import net.yslibrary.monotweety.splash.SplashController

/**
 * Created by yshrsmz on 2016/09/24.
 */
class LauncherActivity : BaseActivity() {

  override val container: ChangeHandlerFrameLayout
    get() = findViewById(R.id.controller_container) as ChangeHandlerFrameLayout

  override val layoutResId: Int
    get() = R.layout.activity_launcher

  override val rootController: Controller
    get() = SplashController()
}