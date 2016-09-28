package net.yslibrary.monotweety.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.ActionBarController
import net.yslibrary.monotweety.base.HasComponent

/**
 * Created by yshrsmz on 2016/09/24.
 */
class SettingController : ActionBarController(), HasComponent<SettingComponent> {

  override val title: String?
    get() = getString(R.string.setting_title)

  override val component: SettingComponent by lazy {
    getComponentProvider<SettingComponent.ComponentProvider>(activity)
        .settingComponent(SettingViewModule())
  }

  override fun onCreate() {
    super.onCreate()
    component.inject(this)
  }

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = inflater.inflate(R.layout.controller_setting, container, false)

    return view
  }
}