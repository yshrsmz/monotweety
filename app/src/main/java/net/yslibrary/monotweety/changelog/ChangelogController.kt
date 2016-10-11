package net.yslibrary.monotweety.changelog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.ActionBarController

/**
 * Created by yshrsmz on 2016/10/11.
 */
class ChangelogController : ActionBarController() {

  override val hasBackButton: Boolean = true

  override val title: String?
    get() = applicationContext.getString(R.string.changelog_title)

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
    return inflater.inflate(R.layout.controller_changelog, container, false)
  }
}