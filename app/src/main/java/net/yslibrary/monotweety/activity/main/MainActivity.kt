package net.yslibrary.monotweety.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.bluelinelabs.conductor.ChangeHandlerFrameLayout
import com.bluelinelabs.conductor.Controller
import net.yslibrary.monotweety.App
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.activity.ActionBarProvider
import net.yslibrary.monotweety.activity.ActivityModule
import net.yslibrary.monotweety.base.BaseActivity
import net.yslibrary.monotweety.base.HasComponent
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.setting.SettingController

class MainActivity : BaseActivity(), ActionBarProvider, HasComponent<MainActivityComponent> {

  companion object {
    fun callingIntent(context: Context): Intent {
      return Intent(context, MainActivity::class.java)
    }
  }

  override val container: ChangeHandlerFrameLayout
    get() = findById(R.id.controller_container)

  override val layoutResId: Int
    get() = R.layout.activity_main

  override val rootController: Controller
    get() = SettingController()

  override val component: MainActivityComponent by lazy {
    DaggerMainActivityComponent.builder()
        .userComponent(App.userComponent(this))
        .activityModule(ActivityModule(this))
        .build()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val toolbar = findById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)

    component.inject(this)
  }
}
