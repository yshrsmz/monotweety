package net.yslibrary.monotweety.setting

import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding.widget.checkedChanges
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.ActionBarController
import net.yslibrary.monotweety.base.HasComponent
import net.yslibrary.monotweety.base.findById

/**
 * Created by yshrsmz on 2016/09/24.
 */
class SettingController : ActionBarController(), HasComponent<SettingComponent> {

  lateinit var bindings: Bindings

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

    bindings = Bindings(view)

    setEvents()

    // enable notification

    // twitter profile

    // start on reboot

    // how to

    // version info

    // license

    // developer

    // to google play

    // padding for ad?

    return view
  }

  fun setEvents() {
    bindings.notificationSwitch
        .checkedChanges()
        .bindToLifecycle()
  }

  inner class Bindings(view: View) {
    val notificationSwitch = view.findById<SwitchCompat>(R.id.notification_switch)
  }
}