package net.yslibrary.monotweety.base

import android.support.v7.app.ActionBar
import android.view.View
import net.yslibrary.monotweety.activity.ActionBarProvider

/**
 * Created by yshrsmz on 2016/09/25.
 */
abstract class ActionBarController : BaseController() {

  open val title: String? = null

  open val shouldShowActionBar: Boolean = true

  fun getActionBar(): ActionBar? {
    val actionBarProvider = activity as ActionBarProvider?
    return actionBarProvider?.let { actionBarProvider.getSupportActionBar() }
  }

  override fun onAttach(view: View) {
    if (shouldShowActionBar) {
      getActionBar()?.show()
    } else {
      getActionBar()?.hide()
    }
    setTitle()
    super.onAttach(view)
  }

  fun setTitle() {
    val actionBar = getActionBar()
    if (actionBar != null && title != null) {
      actionBar.title = title
    }
  }
}