package net.yslibrary.monotweety.status

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.ActionBarController
import timber.log.Timber

/**
 * Created by yshrsmz on 2016/10/01.
 */
class ComposeStatusController(private var status: String? = null) : ActionBarController() {

  override fun onCreate() {
    super.onCreate()
    Timber.d("status: $status")
  }

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = inflater.inflate(R.layout.controller_compose_status, container, false)

    return view
  }
}