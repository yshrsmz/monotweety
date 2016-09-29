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
import net.yslibrary.monotweety.notification.NotificationService
import timber.log.Timber
import javax.inject.Inject

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

  @field:[Inject]
  lateinit var viewModel: SettingViewModel

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
    // make sure to get saved status before subscribes to view events
    viewModel.notificationEnabledChanged
        .bindToLifecycle()
        .doOnNext { bindings.notificationSwitch.isChecked = it }
        .subscribe {
          Timber.d("notification enabled: $it")
          if (it) startNotificationService() else stopNotificationService()
        }

    bindings.notificationSwitch
        .checkedChanges()
        .bindToLifecycle()
        .subscribe { viewModel.onNotificationEnabledChanged(it) }
  }

  fun startNotificationService() {
    applicationContext.startService(NotificationService.callingIntent(activity))
  }

  fun stopNotificationService() {
    applicationContext.stopService(NotificationService.callingIntent(activity))
  }

  inner class Bindings(view: View) {
    val notificationSwitch = view.findById<SwitchCompat>(R.id.notification_switch)
  }
}