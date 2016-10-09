package net.yslibrary.monotweety.activity.launcher

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
import net.yslibrary.monotweety.event.ActivityResult
import net.yslibrary.monotweety.event.NewIntent
import net.yslibrary.monotweety.splash.SplashController

/**
 * Created by yshrsmz on 2016/09/24.
 */
class LauncherActivity : BaseActivity(), ActionBarProvider, HasComponent<LauncherActivityComponent> {

  companion object {
    fun callingIntent(context: Context): Intent {
      val intent = Intent(context, LauncherActivity::class.java)
      return intent
    }
  }

  override val container: ChangeHandlerFrameLayout
    get() = findViewById(R.id.controller_container) as ChangeHandlerFrameLayout

  override val layoutResId: Int
    get() = R.layout.activity_main

  override val rootController: Controller
    get() = SplashController()

  override val component: LauncherActivityComponent by lazy {
    DaggerLauncherActivityComponent.builder()
        .appComponent(App.appComponent(this))
        .activityModule(ActivityModule(this, router))
        .build()
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    intent?.let { activityBus.emit(NewIntent(it)) }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val toolbar = findById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)

    component.inject(this)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    activityBus.emit(ActivityResult(requestCode, resultCode, data))
  }
}