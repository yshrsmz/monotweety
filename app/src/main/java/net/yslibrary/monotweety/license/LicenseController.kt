package net.yslibrary.monotweety.license

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.ActionBarController

/**
 * Created by yshrsmz on 2016/10/10.
 */
class LicenseController : ActionBarController() {


  override fun onCreate() {
    super.onCreate()
  }

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = LayoutInflater.from(container.context).inflate(R.layout.controller_license, container, false)


    return view
  }
}