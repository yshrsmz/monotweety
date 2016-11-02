package net.yslibrary.monotweety.activity.compose

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
import net.yslibrary.monotweety.status.ComposeStatusController

/**
 * Created by yshrsmz on 2016/10/01.
 */
class ComposeActivity : BaseActivity(), ActionBarProvider, HasComponent<ComposeActivityComponent> {

  companion object {
    const val KEY_STATUS = "key_status"

    fun callingIntent(context: Context, status: String? = null): Intent {
      val intent = Intent(context, ComposeActivity::class.java)

      if (!status.isNullOrBlank()) {
        intent.putExtra(KEY_STATUS, status)
      }

      intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
          or Intent.FLAG_ACTIVITY_NEW_TASK)

      return intent
    }
  }

  override val container: ChangeHandlerFrameLayout
    get() = findById(R.id.controller_container)

  override val layoutResId: Int
    get() = R.layout.activity_compose

  override val rootController: Controller
    get() = ComposeStatusController(intent.getStringExtra(KEY_STATUS))

  override val component: ComposeActivityComponent by lazy {
    DaggerComposeActivityComponent.builder()
        .activityModule(ActivityModule(this))
        .userComponent(App.userComponent(this))
        .build()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val toolbar = findById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)

    component.inject(this)
  }
}