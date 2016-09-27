package net.yslibrary.monotweety.activity.main

import android.os.Bundle
import com.bluelinelabs.conductor.ChangeHandlerFrameLayout
import com.bluelinelabs.conductor.Controller
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.activity.ActionBarProvider
import net.yslibrary.monotweety.activity.ActivityModule
import net.yslibrary.monotweety.base.BaseActivity
import net.yslibrary.monotweety.base.HasComponent
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.setting.SettingController

class MainActivity : BaseActivity(), ActionBarProvider, HasComponent<MainActivityComponent> {
  override val container: ChangeHandlerFrameLayout
    get() = findById(R.id.controller_container)

  override val layoutResId: Int
    get() = R.layout.activity_main

  override val rootController: Controller
    get() = SettingController()

  override val component: MainActivityComponent by lazy {
    DaggerMainActivityComponent.builder()
        .activityModule(ActivityModule(this))
        .build()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    component.inject(this)
  }
}
