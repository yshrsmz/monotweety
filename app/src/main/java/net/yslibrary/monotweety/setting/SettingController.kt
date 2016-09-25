package net.yslibrary.monotweety.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.ActionBarController

/**
 * Created by yshrsmz on 2016/09/24.
 */
class SettingController : ActionBarController() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = inflater.inflate(R.layout.controller_setting, container, false)

    return view
  }
}