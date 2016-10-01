package net.yslibrary.monotweety.activity.compose

import android.content.Context
import android.content.Intent
import com.bluelinelabs.conductor.ChangeHandlerFrameLayout
import com.bluelinelabs.conductor.Controller
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.activity.ActionBarProvider
import net.yslibrary.monotweety.base.BaseActivity
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.status.ComposeStatusController

/**
 * Created by yshrsmz on 2016/10/01.
 */
class ComposeActivity : BaseActivity(), ActionBarProvider {

  companion object {
    const val KEY_STATUS = "key_status"

    fun callingIntent(context: Context, status: String? = null): Intent {
      val intent = Intent(context, ComposeActivity::class.java)

      if (!status.isNullOrBlank()) {
        intent.putExtra(KEY_STATUS, status)
      }

      return intent
    }
  }

  override val container: ChangeHandlerFrameLayout
    get() = findById(R.id.controller_container)

  override val layoutResId: Int
    get() = R.layout.activity_compose

  override val rootController: Controller
    get() = ComposeStatusController(intent.getStringExtra(KEY_STATUS))

}